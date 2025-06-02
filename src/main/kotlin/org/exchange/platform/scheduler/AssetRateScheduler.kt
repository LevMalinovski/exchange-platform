package org.exchange.platform.scheduler

import org.exchange.platform.enum.AssetSymbol
import org.exchange.platform.model.Asset
import org.exchange.platform.model.ExchangeRate
import org.exchange.platform.provider.currency.CurrencyRateProvider
import org.exchange.platform.repository.AssetRepository
import org.exchange.platform.repository.ExchangeRateRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class AssetRateScheduler(
    private val assetRepository: AssetRepository,
    private val currencyRateProvider: CurrencyRateProvider,
    private val exchangeRateRepository: ExchangeRateRepository,
) {
    private var assets: MutableList<Asset> = assetRepository.findAllBySymbolNot(AssetSymbol.EUR)

    @Scheduled(fixedDelayString = "\${rate.interval.ms:10000}")
    fun run() {
        this.assets.forEach {
            exchangeRateRepository.save(
                ExchangeRate(
                    asset = it, rate = currencyRateProvider.getRate(it)
                )
            )
        }
    }
}