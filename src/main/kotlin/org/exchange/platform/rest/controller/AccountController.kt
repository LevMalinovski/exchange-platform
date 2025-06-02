package org.exchange.platform.rest.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import org.exchange.platform.command.CreateAccountCommand
import org.exchange.platform.command.GetAccountQuery
import org.exchange.platform.handler.CreateAccountHandler
import org.exchange.platform.handler.GetAccountHandler
import org.exchange.platform.rest.dto.AccountResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "Account", description = "Account creation and retrieval")
@RestController
@RequestMapping("/api/v1/account")
class AccountController(
    private val createAccountHandler: CreateAccountHandler,
    private val getAccountHandler: GetAccountHandler
) {

    @Operation(summary = "Get account by email")
    @GetMapping("/{email}")
    fun getAccount(
        @Valid
        @PathVariable
        @Email(message = "Invalid email format")
        @Schema(description = "Email of the account", example = "test@test.org")
        email: String
    ): ResponseEntity<AccountResponseDto> {
        val account = getAccountHandler.handle(GetAccountQuery(email));
        return ResponseEntity<AccountResponseDto>(
            AccountResponseDto.toDto(account),
            HttpStatus.OK
        )
    }

    @Operation(summary = "Create a new account")
    @PostMapping()
    fun createAccount(@Valid @RequestBody cmd: CreateAccountCommand): ResponseEntity<AccountResponseDto> {
        val account = createAccountHandler.handle(cmd);
        return ResponseEntity<AccountResponseDto>(
            AccountResponseDto.toDto(account),
            HttpStatus.CREATED
        )
    }
}