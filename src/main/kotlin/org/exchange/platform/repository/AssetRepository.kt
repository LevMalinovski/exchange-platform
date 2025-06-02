package org.exchange.platform.repository

import org.exchange.platform.enum.AssetSymbol
import org.exchange.platform.model.Asset
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AssetRepository : JpaRepository<Asset, UUID> {
    fun findBySymbol(symbol: AssetSymbol): Optional<Asset>
    fun findAllBySymbolNot(symbol: AssetSymbol): MutableList<Asset>
}