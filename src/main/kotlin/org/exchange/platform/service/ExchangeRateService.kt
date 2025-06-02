package org.exchange.platform.service

import org.exchange.platform.exception.AppException
import org.exchange.platform.model.Asset
import org.exchange.platform.model.ExchangeRate
import org.exchange.platform.repository.ExchangeRateRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

@Service
class ExchangeRateService(private val exchangeRateRepository: ExchangeRateRepository) {

    fun getRate(fromAsset: Asset, toAsset: Asset): BigDecimal {
        val fromRate = exchangeRateRepository.findLatestByAssetId(fromAsset.id)?.rate
            ?: throw AppException("Rate for $fromAsset not found")
        val toRate = exchangeRateRepository.findLatestByAssetId(toAsset.id)?.rate
            ?: throw AppException("Rate for $toAsset not found")

        return toRate.divide(fromRate, 8, RoundingMode.HALF_UP)
    }

    fun findLatestByAssetId(@Param("assetId") assetId: UUID): ExchangeRate? {
        return exchangeRateRepository.findLatestByAssetId(assetId)
    }
}