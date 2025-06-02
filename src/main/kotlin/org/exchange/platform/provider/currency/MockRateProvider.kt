package org.exchange.platform.provider.currency

import org.exchange.platform.model.Asset
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.*


@Component
class MockRateProvider : CurrencyRateProvider {
    override fun getRate(asset: Asset): BigDecimal {
        val randomValue = Random().nextDouble(0.1, 50000.0)
        return BigDecimal.valueOf(randomValue)
    }
}