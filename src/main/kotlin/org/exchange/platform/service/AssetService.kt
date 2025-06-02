package org.exchange.platform.service

import org.exchange.platform.enum.AssetSymbol
import org.exchange.platform.exception.AssetException
import org.exchange.platform.model.Asset
import org.exchange.platform.repository.AssetRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class AssetService(private val assetRepository: AssetRepository) {
    private val log = LoggerFactory.getLogger(AssetService::class.java)

    fun findBySymbol(assetSymbol: String): Optional<Asset> {
        return assetRepository.findBySymbol(AssetSymbol.valueOf(assetSymbol))
    }

    fun getAsset(assetSymbol: String): Asset {
        return assetRepository.findBySymbol(AssetSymbol.valueOf(assetSymbol)).orElseThrow({
            log.warn("Asset not found: $assetSymbol")
            AssetException("Asset not found")
        })
    }
}