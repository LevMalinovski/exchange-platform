package org.exchange.platform.orchestrator

import org.exchange.platform.command.WithdrawCommand
import org.exchange.platform.enum.AssetType
import org.exchange.platform.enum.TransactionType
import org.exchange.platform.exception.AccountBalanceException
import org.exchange.platform.model.AccountBalance
import org.exchange.platform.service.AccountBalanceService
import org.exchange.platform.service.AccountService
import org.exchange.platform.service.AssetService
import org.exchange.platform.service.BalanceTransactionService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class WithdrawOrchestrator(
    private val accountService: AccountService,
    private val accountBalanceService: AccountBalanceService,
    private val transactionService: BalanceTransactionService,
    private val assetService: AssetService,
) {
    private val log = LoggerFactory.getLogger(WithdrawOrchestrator::class.java)

    fun withdraw(command: WithdrawCommand) {
        log.info("Handling withdraw: $command")
        validateAmount(command)
        validateAsset(command.assetSymbol)
        val account = accountService.getAccount(command.accountEmail)
        val balance = accountBalanceService.get(account.id, command.assetSymbol)
        validateBalance(balance, command)
        // Create a transaction record of the withdrawal
        log.info("Creating withdrawal transaction for user ${account.id}, asset ${command.assetSymbol}, amount ${command.amount}")
        transactionService.record(account.id, command.amount, balance.asset, TransactionType.WITHDRAW)
        accountBalanceService.decreaseBalance(balance, command.amount)
    }

    private fun validateBalance(
        balance: AccountBalance,
        command: WithdrawCommand
    ) {
        if (balance.amount < command.amount) {
            log.error("Withdraw amount can not be less than zero. Email: ${command.accountEmail}")
            throw AccountBalanceException("Wthdraw amount must be greater than zero.")
        }
    }

    private fun validateAmount(command: WithdrawCommand) {
        if (command.amount <= BigDecimal.ZERO) {
            log.error("Withdraw amount can not be less than zero. Email: ${command.accountEmail}")
            throw AccountBalanceException("Wthdraw amount must be greater than zero.")
        }
    }

    private fun validateAsset(assetSymbol: String) {
        if (assetService.getAsset(assetSymbol).type != AssetType.FIAT) {
            log.error("Withdraw asset type $assetSymbol is not a fiat.")
            throw AccountBalanceException("Withdraw asset type $assetSymbol is not a fiat.")
        }
    }
}