package org.exchange.platform.command

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class CreateAccountCommand(
    @field:Email @field:NotBlank val email: String
) : Command