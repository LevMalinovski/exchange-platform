package org.exchange.platform.config

import io.micrometer.core.aop.TimedAspect
import io.micrometer.core.instrument.MeterRegistry
import org.exchange.platform.provider.currency.CurrencyRateProvider
import org.exchange.platform.provider.currency.MockRateProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling


@Configuration
@EnableScheduling
class AppConfig {
    @Bean
    fun timedAspect(registry: MeterRegistry): TimedAspect {
        return TimedAspect(registry)
    }

    @Bean
    fun currencyRateProvider(): CurrencyRateProvider {
        return MockRateProvider();
    }
}