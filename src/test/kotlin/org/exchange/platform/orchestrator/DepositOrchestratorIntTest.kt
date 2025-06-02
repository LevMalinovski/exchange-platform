package org.exchange.platform.orchestrator

import org.exchange.platform.BasicIntegrationTest
import org.exchange.platform.command.DepositCommand
import org.exchange.platform.enum.AssetSymbol
import org.exchange.platform.enum.AssetType
import org.exchange.platform.exception.AccountBalanceException
import org.exchange.platform.model.Account
import org.exchange.platform.model.AccountBalance
import org.exchange.platform.model.Asset
import org.exchange.platform.repository.AccountBalanceRepository
import org.exchange.platform.repository.AccountRepository
import org.exchange.platform.repository.AssetRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.util.*

class DepositOrchestratorIntTest : BasicIntegrationTest() {

    @Autowired
    lateinit var depositOrchestrator: DepositOrchestrator

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Autowired
    lateinit var assetRepository: AssetRepository

    @Autowired
    lateinit var accountBalanceRepository: AccountBalanceRepository

    private lateinit var account: Account
    private lateinit var asset: Asset
    private lateinit var accountBalance: AccountBalance

    @BeforeEach
    fun setup() {
        account = accountRepository.save(Account(email = "test-${UUID.randomUUID()}@test.com"))
        asset = assetRepository.findBySymbol(AssetSymbol.EUR)
            .orElseGet {
                assetRepository.save(Asset(symbol = AssetSymbol.EUR, name = "Euro", type = AssetType.FIAT))
            }
        accountBalance = AccountBalance(account = account, asset = asset)
        accountBalanceRepository.save(accountBalance)
    }

    @Test
    fun `should deposit fiat to account`() {
        // Given
        val depositAmount = BigDecimal.valueOf(150.00)
        val command = DepositCommand(account.email, asset.symbol.name, depositAmount)
        // When
        depositOrchestrator.deposit(command)
        // Then
        val balance = accountBalanceRepository.findById(accountBalance.id).get()
        assertEquals(depositAmount, balance.amount)
    }

    @Test
    fun `should not deposit fiat to account if amount less 0`() {
        // Given
        val depositAmount = BigDecimal.valueOf(-1.00)
        val command = DepositCommand(account.email, asset.symbol.name, depositAmount)
        // When
        val exception = assertThrows<AccountBalanceException> {
            depositOrchestrator.deposit(command)
        }
        // Then
        assertEquals("Deposit amount must be greater than zero.", exception.message)
        val balance = accountBalanceRepository.findById(accountBalance.id).get()
        assertEquals(BigDecimal.ZERO, balance.amount)
    }

    @Test
    fun `should deposit only fiat to account`() {
        // Given
        asset = assetRepository.findBySymbol(AssetSymbol.BTC)
            .orElseGet {
                assetRepository.save(Asset(symbol = AssetSymbol.BTC, name = "BTC", type = AssetType.CRYPTO))
            }
        val depositAmount = BigDecimal.valueOf(10.00)
        val command = DepositCommand(account.email, asset.symbol.name, depositAmount)
        // When
        val exception = assertThrows<AccountBalanceException> {
            depositOrchestrator.deposit(command)
        }
        // Then
        val balance = accountBalanceRepository.findById(accountBalance.id).get()
        assertEquals("Deposit asset type BTC is not a fiat.", exception.message)
        assertEquals(BigDecimal.ZERO, balance.amount)
    }
}