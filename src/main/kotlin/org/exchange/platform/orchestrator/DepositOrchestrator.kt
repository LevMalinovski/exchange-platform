package org.exchange.platform.orchestrator

import jakarta.transaction.Transactional
import org.exchange.platform.command.DepositCommand
import org.exchange.platform.enum.AssetType
import org.exchange.platform.enum.TransactionType
import org.exchange.platform.exception.AccountBalanceException
import org.exchange.platform.service.AccountBalanceService
import org.exchange.platform.service.AccountService
import org.exchange.platform.service.AssetService
import org.exchange.platform.service.BalanceTransactionService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class DepositOrchestrator(
    private val accountService: AccountService,
    private val accountBalanceService: AccountBalanceService,
    private val transactionService: BalanceTransactionService,
    private val assetService: AssetService,
) {
    private val log = LoggerFactory.getLogger(DepositOrchestrator::class.java)

    @Transactional
    fun deposit(command: DepositCommand) {
        try {
            log.info("Handling deposit: $command")
            validateAmount(command)
            validateAsset(command.assetSymbol);

            val account = accountService.getAccount(command.accountEmail)
            val balance = accountBalanceService.get(account.id, command.assetSymbol)
            // Create a transaction record of the deposit
            log.info("Creating deposit transaction for user ${account.id}, asset ${command.assetSymbol}, amount ${command.amount}")
            transactionService.record(account.id, command.amount, balance.asset, TransactionType.DEPOSIT)
            accountBalanceService.increaseBalance(balance, command.amount)
        } catch (e: Exception) {
            log.error("Deposit error", e)
            throw e // transaction will rollback, lock will be released
        }
    }

    private fun validateAmount(command: DepositCommand) {
        if (command.amount <= BigDecimal.ZERO) {
            log.error("Deposit amount can not be less than zero. Email: ${command.accountEmail}")
            throw AccountBalanceException("Deposit amount must be greater than zero.")
        }
    }

    private fun validateAsset(assetSymbol: String) {
        if (assetService.getAsset(assetSymbol).type != AssetType.FIAT) {
            log.error("Deposit asset type $assetSymbol is not a fiat.")
            throw AccountBalanceException("Deposit asset type $assetSymbol is not a fiat.")
        }
    }
}