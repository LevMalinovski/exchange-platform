package org.exchange.platform.service

import org.exchange.platform.BasicIntegrationTest
import org.exchange.platform.enum.AssetSymbol
import org.exchange.platform.enum.AssetType
import org.exchange.platform.exception.AccountBalanceException
import org.exchange.platform.model.Account
import org.exchange.platform.model.AccountBalance
import org.exchange.platform.model.Asset
import org.exchange.platform.repository.AccountBalanceRepository
import org.exchange.platform.repository.AccountRepository
import org.exchange.platform.repository.AssetRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import kotlin.test.assertEquals


class AccountBalanceIntTest() : BasicIntegrationTest() {
    @Autowired
    private lateinit var assetRepository: AssetRepository

    @Autowired
    private lateinit var accountBalanceRepository: AccountBalanceRepository

    @Autowired
    private lateinit var accountBalanceService: AccountBalanceService

    @Autowired
    private lateinit var accountRepository: AccountRepository

    @BeforeEach
    fun setup() {
        accountRepository.deleteAll()
        accountBalanceRepository.deleteAll();
        assetRepository.deleteAll()
    }

    @Test
    fun testShouldCreate() {
        // Given
        val account = Account(email = "test-1@test.org")
        accountRepository.save<Account>(account)
        val asset = Asset(symbol = AssetSymbol.EUR, name = "EUR", type = AssetType.FIAT);
        assetRepository.save<Asset>(asset)
        // When
        val balance = accountBalanceService.create("test-1@test.org", AssetSymbol.EUR.name);
        // Then
        assertEquals(BigDecimal.ZERO, balance.amount)
        assertEquals(account.id, balance.account.id)
    }

    @Test
    fun testShouldIncreateBalance() {
        // Given
        val account = Account(email = "test-1@test.org")
        accountRepository.save<Account>(account)
        val asset = Asset(symbol = AssetSymbol.EUR, name = "EUR", type = AssetType.FIAT);
        assetRepository.save<Asset>(asset)
        val accountBalance = AccountBalance(
            account = account,
            amount = BigDecimal.valueOf(100.00),
            asset = asset
        );
        accountBalanceRepository.save<AccountBalance>(accountBalance)
        // When
        val accountBalanceResult = accountBalanceService.increaseBalance(accountBalance, BigDecimal.valueOf(100.12))
        // Then
        assertEquals(BigDecimal.valueOf(200.12), accountBalanceResult.amount)
        val accountBalanceDB = accountBalanceRepository.findByAccountIdAndAssetId(account.id, asset.id).get()
        assertEquals(BigDecimal.valueOf(200.12), accountBalanceDB.amount)
    }

    @Test
    fun testShouldDecreaseBalance() {
        // Given
        val account = Account(email = "test-1@test.org")
        accountRepository.save<Account>(account)
        val asset = Asset(symbol = AssetSymbol.EUR, name = "EUR", type = AssetType.FIAT);
        assetRepository.save<Asset>(asset)
        accountBalanceRepository.deleteAll();
        val accountBalance = AccountBalance(
            account = account,
            amount = BigDecimal.valueOf(876.90),
            asset = asset
        );
        accountBalanceRepository.save<AccountBalance>(accountBalance)
        // When
        val accountBalanceResult = accountBalanceService.decreaseBalance(accountBalance, BigDecimal.valueOf(100.12))
        // Then
        assertEquals(BigDecimal.valueOf(776.78), accountBalanceResult.amount)
        val accountBalanceDB = accountBalanceRepository.findByAccountIdAndAssetId(account.id, asset.id).get()
        assertEquals(BigDecimal.valueOf(776.78), accountBalanceDB.amount)
    }

    @Test
    fun testShouldNotDecreaseBalanceIfBalanceNotEnough() {
        // Given
        val account = Account(email = "test-1@test.org")
        accountRepository.save<Account>(account)
        val asset = Asset(symbol = AssetSymbol.EUR, name = "EUR", type = AssetType.FIAT);
        assetRepository.save<Asset>(asset)
        accountBalanceRepository.deleteAll();
        val accountBalance = AccountBalance(
            account = account,
            amount = BigDecimal.valueOf(76.90),
            asset = asset
        );
        accountBalanceRepository.save<AccountBalance>(accountBalance)
        // When
        val exception = assertThrows<AccountBalanceException> {
            accountBalanceService.decreaseBalance(accountBalance, BigDecimal.valueOf(100.12))
        }
        // Then
        assertEquals("Not enough balance", exception.message)
    }
}