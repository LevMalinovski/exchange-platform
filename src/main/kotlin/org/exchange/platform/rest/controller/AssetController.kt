package org.exchange.platform.rest.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.exchange.platform.command.BuyAssetCommand
import org.exchange.platform.command.CreateAccountBalanceCommand
import org.exchange.platform.command.SellAssetCommand
import org.exchange.platform.enum.OperationType
import org.exchange.platform.service.OperationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Asset", description = "Operations related to asset balances")
@RestController
@RequestMapping("/api/v1/asset")
class AssetController(private val operationService: OperationService) {
    @Operation(
        summary = "Create asset balance for a user",
        description = "Creates an empty balance entry for a specific asset under a user's account"
    )
    @PostMapping()
    fun createAccountAssetBalance(@Valid @RequestBody cmd: CreateAccountBalanceCommand): ResponseEntity<String> {
        operationService.add(OperationType.CREATE_BALANCE, cmd)
        return ResponseEntity.accepted().build()
    }

    @PostMapping("/buy")
    fun buyAsset(@Valid @RequestBody cmd: BuyAssetCommand): ResponseEntity<String> {
        operationService.add(OperationType.BUY, cmd)
        return ResponseEntity.accepted().build()
    }

    @PostMapping("/sell")
    fun sellAsset(@Valid @RequestBody cmd: SellAssetCommand): ResponseEntity<String> {
        operationService.add(OperationType.SELL, cmd)
        return ResponseEntity.accepted().build()
    }
}