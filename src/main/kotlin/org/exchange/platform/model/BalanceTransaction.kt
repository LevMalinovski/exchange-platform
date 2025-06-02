package org.exchange.platform.model

import jakarta.persistence.*
import org.exchange.platform.enum.TransactionType
import java.math.BigDecimal
import java.util.*

@Entity
@Table(name = "balance_transactions")
data class BalanceTransaction(
    @Id val id: UUID = UUID.randomUUID(),
    @Column(name = "account_id") val userId: UUID,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id") val asset: Asset,
    val amount: BigDecimal,
    @Enumerated(EnumType.STRING)
    val type: TransactionType,
    val reference: UUID? = null,
    @Column(name = "created_at") val createdAt: Date = Date()
)