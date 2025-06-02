package org.exchange.platform.orchestrator

import org.exchange.platform.command.SellAssetCommand
import org.exchange.platform.enum.AssetSymbol
import org.exchange.platform.enum.AssetType
import org.exchange.platform.enum.TransactionType
import org.exchange.platform.exception.AccountBalanceException
import org.exchange.platform.exception.SellAssetException
import org.exchange.platform.model.Asset
import org.exchange.platform.service.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.RoundingMode

@Component
class SellAssetOrchestrator(
    private val assetService: AssetService,
    private val exchangeRateService: ExchangeRateService,
    private val accountBalanceService: AccountBalanceService,
    private val balanceTransactionService: BalanceTransactionService,
    private val accountService: AccountService
) {
    private val log = LoggerFactory.getLogger(SellAssetOrchestrator::class.java)

    private val defaultFiatCurrency = "EUR"

    @Transactional
    fun handle(command: SellAssetCommand) {
        try {
            val account = accountService.getAccount(command.accountEmail)
            log.info("Starting sell operation: $command")
            val sellAssetBalance =
                accountBalanceService.lockByAccountIdAndAssetSymbol(
                    account.id,
                    AssetSymbol.valueOf(command.sellAssetSymbol)
                )
                    .orElseThrow { AccountBalanceException("Balance not found") }
            if (sellAssetBalance.amount < command.amount) {
                throw AccountBalanceException("Insufficient asset to sell")
            }
            val sellAsset = sellAssetBalance.asset
            if (sellAsset.type == AssetType.FIAT) {
                throw SellAssetException("Cannot sell fiat asset")
            }
            // Convert to EUR (or other default fiat)
            val fiatAsset = getAsset(defaultFiatCurrency)
            val rate = exchangeRateService.getRate(sellAsset, fiatAsset)
            val receivedAmount = command.amount.multiply(rate).setScale(8, RoundingMode.HALF_UP)

            // Decrease asset
            accountBalanceService.decreaseBalance(sellAssetBalance, command.amount)

            // Increase EUR
            val fiatBalance = accountBalanceService.get(account.id, fiatAsset.symbol.name)
            accountBalanceService.increaseBalance(fiatBalance, receivedAmount)
            balanceTransactionService.record(account.id, command.amount, sellAsset, TransactionType.SELL)
        } catch (e: Exception) {
            log.error("Sell operation failed: ${e.message}", e)
            throw e // transaction will rollback, lock will be released
        }
    }

    private fun getAsset(assetSymbol: String): Asset {
        return assetService.getAsset(assetSymbol)
    }
}