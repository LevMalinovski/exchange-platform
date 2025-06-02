package org.exchange.platform.rest.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.exchange.platform.command.DepositCommand
import org.exchange.platform.command.WithdrawCommand
import org.exchange.platform.enum.OperationType
import org.exchange.platform.service.OperationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Balance", description = "Deposit and withdraw operations")
@RestController
@RequestMapping("/api/v1/balance")
class AccountBalanceController(private val operationService: OperationService) {
    @Operation(
        summary = "Deposit funds to account",
        description = "Schedules a deposit operation which will be processed asynchronously"
    )
    @PostMapping("/deposit")
    fun deposit(@Valid @RequestBody cmd: DepositCommand): ResponseEntity<String> {
        operationService.add(OperationType.DEPOSIT, cmd)
        return ResponseEntity.accepted().build();
    }

    @Operation(
        summary = "Withdraw funds from account",
        description = "Schedules a withdraw operation which will be processed asynchronously"
    )
    @PostMapping("/withdraw")
    fun withdraw(@Valid @RequestBody cmd: WithdrawCommand): ResponseEntity<String> {
        operationService.add(OperationType.WITHDRAW, cmd)
        return ResponseEntity.accepted().build();
    }
}