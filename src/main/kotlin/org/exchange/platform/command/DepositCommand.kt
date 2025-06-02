package org.exchange.platform.command

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import org.exchange.platform.rest.validation.ValidAssetType
import java.math.BigDecimal

data class DepositCommand(
    @field:Schema(description = "Email of the account owner", example = "test@test.org")
    val accountEmail: String,
    @field:Schema(description = "Symbol of the asset to deposit", example = "EUR")
    @field:NotBlank
    @field:ValidAssetType(message = "Only fiat assets are allowed")
    val assetSymbol: String,
    @field:Schema(description = "Amount to deposit", example = "100.00")
    @field:DecimalMin(value = "0.00000000001", inclusive = true, message = "Amount must be greater than 0")
    val amount: BigDecimal
) : Command