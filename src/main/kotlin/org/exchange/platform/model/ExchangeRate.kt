package org.exchange.platform.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.util.*

@Entity
@Table(name = "exchange_rates")
data class ExchangeRate(
    @Id val id: UUID = UUID.randomUUID(),
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id")
    val asset: Asset,
    val rate: BigDecimal,
    @Column(name = "created_at") val createdAt: Date = Date()
)