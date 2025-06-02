package org.exchange.platform.orchestrator

import org.exchange.platform.command.BuyAssetCommand
import org.exchange.platform.enum.AssetType
import org.exchange.platform.enum.TransactionType
import org.exchange.platform.exception.AccountBalanceException
import org.exchange.platform.exception.BuyAssetException
import org.exchange.platform.model.Asset
import org.exchange.platform.service.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.RoundingMode

@Component
class BuyAssetOrchestrator(
    private val assetService: AssetService,
    private val exchangeRateService: ExchangeRateService,
    private val accountBalanceService: AccountBalanceService,
    private val balanceTransactionService: BalanceTransactionService,
    private val accountService: AccountService
) {
    private val log = LoggerFactory.getLogger(BuyAssetOrchestrator::class.java)

    @Transactional
    fun handle(command: BuyAssetCommand) {
        try {
            val account = accountService.getAccount(command.accountEmail)
            val fromAsset = getFromAsset(command.fromAssetSymbol)
            val buyAsset = getBuyAsset(command.buyAssetSymbol)
            // Cannot buy FIAT, only crypto/stocks/commodities
            if (buyAsset.type == AssetType.FIAT) {
                log.warn("Attempt to buy fiat asset: ${buyAsset.symbol}")
                throw BuyAssetException("Buying fiat assets is not allowed")
            }
            val lockedBalance = accountBalanceService.lockByAccountIdAndAssetSymbol(account.id, fromAsset.symbol)
                .orElseThrow { AccountBalanceException("Balance not found") }

            // Get rate FROM -> TO (e.g., USD -> BTC)
            val rate = exchangeRateService.getRate(fromAsset, buyAsset);
            // Calculate how much buyAsset will be bought for 'amount' in fromAsset
            val buyAmount = command.amount.divide(rate, 8, RoundingMode.HALF_UP)
            log.info("Buying $buyAmount ${buyAsset.symbol} using ${command.amount} ${fromAsset.symbol} at rate $rate. Account ${account.id}")

            // 1. Check if user has enough balance
            if (lockedBalance.amount < command.amount) {
                throw AccountBalanceException("Insufficient funds")
            }
            accountBalanceService.decreaseBalance(lockedBalance, command.amount)
            val buyBalance = accountBalanceService.getOrCreate(account.id, buyAsset.symbol.name)
            accountBalanceService.increaseBalance(buyBalance, buyAmount)
            balanceTransactionService.record(account.id, buyAmount, buyAsset, TransactionType.BUY)
        } catch (e: Exception) {
            log.error("Buy asset operation failed: ${e.message}")
            throw e // transaction will rollback, lock will be released
        }
    }

    private fun getFromAsset(assetSymbol: String): Asset {
        return assetService.getAsset(assetSymbol)
    }

    private fun getBuyAsset(assetSymbol: String): Asset {
        return assetService.getAsset(assetSymbol)
    }
}