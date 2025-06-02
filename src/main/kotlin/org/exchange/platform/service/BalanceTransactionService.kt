package org.exchange.platform.service

import org.exchange.platform.enum.TransactionType
import org.exchange.platform.model.Asset
import org.exchange.platform.model.BalanceTransaction
import org.exchange.platform.repository.BalanceTransactionRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

@Service
class BalanceTransactionService(private val balanceTransactionRepository: BalanceTransactionRepository) {
    fun record(accountId: UUID, amount: BigDecimal, asset: Asset, type: TransactionType): BalanceTransaction {
        return balanceTransactionRepository.save(
            BalanceTransaction(
                userId = accountId,
                amount = amount,
                asset = asset,
                type = type
            )
        )
    }
}