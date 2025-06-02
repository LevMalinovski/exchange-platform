package org.exchange.platform.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.persistence.FetchType.LAZY
import java.math.BigDecimal
import java.util.*

@Entity
@Table(name = "account_balances")
data class AccountBalance(
    @Id val id: UUID = UUID.randomUUID(),
    @ManyToOne(fetch = LAZY)
    @JsonIgnore
    @JoinColumn(name = "account_id", nullable = false)
    val account: Account,
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "asset_id")
    val asset: Asset,
    var amount: BigDecimal = BigDecimal.ZERO,
    @Column(name = "created_at") val createdAt: Date = Date()
)