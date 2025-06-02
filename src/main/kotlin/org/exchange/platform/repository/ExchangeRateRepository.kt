package org.exchange.platform.repository

import org.exchange.platform.model.ExchangeRate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ExchangeRateRepository : JpaRepository<ExchangeRate, UUID> {
    @Query(
        """
    SELECT * FROM exchange_rates 
    WHERE asset_id = :assetId 
    ORDER BY id DESC 
    LIMIT 1
    """,
        nativeQuery = true
    )
    fun findLatestByAssetId(@Param("assetId") assetId: UUID): ExchangeRate?
}