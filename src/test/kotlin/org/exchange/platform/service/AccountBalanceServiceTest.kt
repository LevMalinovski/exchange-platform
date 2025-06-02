package org.exchange.platform.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.exchange.platform.enum.AssetSymbol
import org.exchange.platform.enum.AssetType
import org.exchange.platform.model.Account
import org.exchange.platform.model.AccountBalance
import org.exchange.platform.model.Asset
import org.exchange.platform.repository.AccountBalanceRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.*

class AccountBalanceServiceTest {
    private val accountService: AccountService = mockk();
    private val assetService: AssetService = mockk();
    private val accountBalanceRepository: AccountBalanceRepository = mockk();
    private val accountBalanceService =
        AccountBalanceService(accountService, assetService, accountBalanceRepository)

    @Test
    fun testShouldCreate() {
        // Given
        val account = Account(email = "test@test.org")
        val asset = Asset(symbol = AssetSymbol.EUR, name = "EUR", type = AssetType.FIAT)
        val accountBalance = AccountBalance(account = account, asset = asset)
        every { accountService.getAccount(any<String>()) } returns account
        every { assetService.getAsset(any()) } returns asset
        every { accountBalanceRepository.existsAccountBalanceByAccountIdAndAssetId(any(), any()) } returns false
        every { accountBalanceRepository.save<AccountBalance>(any()) } returns accountBalance
        // When
        val balance = accountBalanceService.create("test@test.org", "EUR")
        // Then
        verify(exactly = 1) { accountBalanceRepository.save(any()) };
        assertEquals(balance.account, accountBalance.account)
        assertEquals(balance.asset, accountBalance.asset)
        assertEquals(balance.amount, BigDecimal.ZERO)
    }

    @Test
    fun testShouldGetBalance() {
        // Given
        val account = Account(email = "test@test.org")
        val asset = Asset(symbol = AssetSymbol.EUR, name = "EUR", type = AssetType.FIAT)
        val accountBalance = AccountBalance(account = account, asset = asset)
        every { assetService.findBySymbol("EUR") } returns Optional.of<Asset>(asset)
        every {
            accountBalanceRepository.findByAccountIdAndAssetId(
                account.id,
                asset.id
            )
        } returns Optional.of<AccountBalance>(accountBalance)
        // When
        val balance = accountBalanceService.get(account.id, asset.name)
        // Then
        assertEquals(balance, accountBalance)
    }

    @Test
    fun testShouldIncreaseBalance() {
        // Given
        val account = Account(email = "test@test.org")
        val asset = Asset(symbol = AssetSymbol.EUR, name = "EUR", type = AssetType.FIAT)
        val accountBalance = AccountBalance(account = account, asset = asset)
        every { accountBalanceRepository.save(accountBalance) } returns accountBalance
        every { accountBalanceRepository.lockByAccountIdAndAssetSymbol(any(), any()) } returns Optional.of(
            accountBalance
        )
        // When
        accountBalanceService.increaseBalance(accountBalance, BigDecimal.valueOf(100))
        // Then
        verify(exactly = 1) { accountBalanceRepository.save(accountBalance) };
    }

    @Test
    fun testShouldDecreaseBalance() {
        // Given
        val account = Account(email = "test@test.org")
        val asset = Asset(symbol = AssetSymbol.EUR, name = "EUR", type = AssetType.FIAT)
        val accountBalance = AccountBalance(account = account, asset = asset, amount = BigDecimal.valueOf(200))
        every { accountBalanceRepository.save(accountBalance) } returns accountBalance
        every { accountBalanceRepository.lockByAccountIdAndAssetSymbol(any(), any()) } returns Optional.of(
            accountBalance
        )
        // When
        accountBalanceService.decreaseBalance(accountBalance, BigDecimal.valueOf(100))
        // Then
        verify(exactly = 1) { accountBalanceRepository.save(accountBalance) };
    }
}