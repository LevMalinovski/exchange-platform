package org.exchange.platform.provider.currency

import org.exchange.platform.model.Asset
import java.math.BigDecimal

interface CurrencyRateProvider {
    fun getRate(asset: Asset): BigDecimal
}