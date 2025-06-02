package org.exchange.platform.model

import jakarta.persistence.*
import org.exchange.platform.enum.AssetSymbol
import org.exchange.platform.enum.AssetType
import java.util.*

@Entity
@Table(name = "assets")
data class Asset(
    @Id val id: UUID = UUID.randomUUID(),
    @Enumerated(EnumType.STRING)
    val symbol: AssetSymbol,
    val name: String,
    @Enumerated(EnumType.STRING)
    val type: AssetType,
)