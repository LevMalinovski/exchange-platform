package org.exchange.platform.service

import org.exchange.platform.enum.AssetSymbol
import org.exchange.platform.exception.AccountBalanceException
import org.exchange.platform.exception.AssetException
import org.exchange.platform.model.Account
import org.exchange.platform.model.AccountBalance
import org.exchange.platform.repository.AccountBalanceRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.*

@Service
@Transactional
class AccountBalanceService(
    private val accountService: AccountService,
    private val assetService: AssetService,
    private val accountBalanceRepository: AccountBalanceRepository,
) {
    private val log = LoggerFactory.getLogger(AccountBalanceService::class.java)

    fun create(accountId: UUID, assetSymbol: String): AccountBalance {
        val account = accountService.getAccount(accountId)
        return create(account, assetSymbol)
    }

    fun create(accountEmail: String, assetSymbol: String): AccountBalance {
        val account = accountService.getAccount(accountEmail)
        return create(account, assetSymbol)
    }

    fun create(account: Account, assetSymbol: String): AccountBalance {
        val asset = assetService.getAsset(assetSymbol)
        if (accountBalanceRepository.existsAccountBalanceByAccountIdAndAssetId(account.id, asset.id)) {
            log.warn("Account with id ${account.id} and asset ${asset.id} already exists.")
            throw AccountBalanceException("Asset balance exists")
        }
        val accountBalance = AccountBalance(account = account, asset = asset)
        accountBalanceRepository.save(accountBalance)
        return accountBalance
    }

    fun get(accountId: UUID, assetSymbol: String): AccountBalance {
        val asset =
            assetService.findBySymbol(assetSymbol).orElseThrow { AssetException("Asset not found") }
        return accountBalanceRepository.findByAccountIdAndAssetId(accountId, asset.id)
            .orElseThrow { AccountBalanceException("Account balance not found") }
    }

    fun getOrCreate(accountId: UUID, assetSymbol: String): AccountBalance {
        val asset =
            assetService.findBySymbol(assetSymbol).orElseThrow { AssetException("Asset not found") }
        val accountBalance = accountBalanceRepository.findByAccountIdAndAssetId(accountId, asset.id)

        if (!accountBalance.isPresent) {
            return create(accountId, asset.symbol.name)
        }
        return accountBalance.get()
    }

    fun get(accountEmail: String, assetSymbol: String): AccountBalance {
        val account = accountService.getAccount(accountEmail)
        return get(account.id, assetSymbol)
    }

    fun increaseBalance(
        accountBalance: AccountBalance,
        amount: BigDecimal
    ): AccountBalance {
        log.info("Account balance updated: ID: ${accountBalance.id}, amount: $amount")
        if (amount <= BigDecimal.ZERO) {
            throw AccountBalanceException("Amount can't be negative")
        }
        lockByAccountIdAndAssetSymbol(accountBalance.account.id, accountBalance.asset.symbol)
        accountBalance.amount = accountBalance.amount + amount;
        accountBalanceRepository.save(accountBalance)
        return accountBalance
    }

    fun decreaseBalance(
        accountBalance: AccountBalance,
        amount: BigDecimal
    ): AccountBalance {
        log.info("Account balance updated: ID: ${accountBalance.id}, amount: $amount")
        if (accountBalance.amount < amount) {
            throw AccountBalanceException("Not enough balance")
        }
        println("AAAA ${accountBalance.asset.name} - ${accountBalance.asset.symbol}")
        lockByAccountIdAndAssetSymbol(accountBalance.account.id, accountBalance.asset.symbol)
        accountBalance.amount = accountBalance.amount - amount;
        accountBalanceRepository.save(accountBalance)
        return accountBalance
    }

    fun lockByAccountIdAndAssetSymbol(accountId: UUID, symbol: AssetSymbol): Optional<AccountBalance> {
        return accountBalanceRepository.lockByAccountIdAndAssetSymbol(accountId, symbol)
    }
}