package org.exchange.platform.repository

import org.exchange.platform.model.BalanceTransaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface BalanceTransactionRepository : JpaRepository<BalanceTransaction, UUID> {
}