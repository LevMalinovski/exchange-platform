package org.exchange.platform.command

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import org.exchange.platform.rest.validation.AssetExistsType
import org.exchange.platform.rest.validation.ValidAssetType
import java.math.BigDecimal

data class SellAssetCommand(
    @field:Schema(description = "Email of the account owner", example = "test@test.org")
    val accountEmail: String,
    @field:Schema(description = "Symbol of the asset to sell", example = "BTC")
    @field:NotBlank
    @field:AssetExistsType
    @field:ValidAssetType(allowFiat = false, message = "Not fiat allowed")
    val sellAssetSymbol: String,
    @field:Schema(description = "Amount to sell", example = "10.00")
    @field:DecimalMin(value = "0.00000000001", inclusive = true, message = "Amount must be greater than 0")
    val amount: BigDecimal
) : Command