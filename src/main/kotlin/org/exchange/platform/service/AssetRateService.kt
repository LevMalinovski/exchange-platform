package org.exchange.platform.service

import org.exchange.platform.exception.AssetRateException
import org.exchange.platform.model.Asset
import org.exchange.platform.repository.ExchangeRateRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class AssetRateService(private val exchangeRateRepository: ExchangeRateRepository) {
    fun getRate(asset: Asset): BigDecimal {
        return exchangeRateRepository.findLatestByAssetId(asset.id)
            ?.rate ?: throw AssetRateException("Asset ${asset.id} not found")
    }
}