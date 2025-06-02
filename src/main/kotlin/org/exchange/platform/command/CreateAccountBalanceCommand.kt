package org.exchange.platform.command

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

data class CreateAccountBalanceCommand(
    @field:NotBlank
    @Schema(description = "User email for the account", example = "test@test.org")
    val accountEmail: String,
    @field:NotBlank
    @Schema(description = "Asset symbol to create balance for", example = "BTC")
    val assetSymbol: String
) : Command