package org.exchange.platform.rest.dto

import java.math.BigDecimal

data class AccountBalanceResponseDto(
    val balance: BigDecimal,
    val asset: AssetResponseDto,
)