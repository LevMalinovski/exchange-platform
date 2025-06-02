package org.exchange.platform

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication
class ExchangePlatformApplication

fun main(args: Array<String>) {
    runApplication<ExchangePlatformApplication>(*args)
}
