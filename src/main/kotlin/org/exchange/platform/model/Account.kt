package org.exchange.platform.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import java.util.*

@Entity
@Table(name = "accounts")
data class Account(
    @Id val id: UUID = UUID.randomUUID(),
    @NotBlank(message = "Email is mandatory")
    val email: String,
    val createdAt: Date = Date(),
    @OneToMany(mappedBy = "account", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = false)
    val balances: MutableList<AccountBalance> = mutableListOf()
)