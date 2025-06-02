package org.exchange.platform.handler

import org.exchange.platform.command.CreateAccountBalanceCommand
import org.exchange.platform.enum.OperationType
import org.exchange.platform.model.AccountBalance
import org.exchange.platform.service.AccountBalanceService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CreateAccountBalanceHandler(
    private val accountBalanceService: AccountBalanceService
) : Handler<CreateAccountBalanceCommand> {
    private val log = LoggerFactory.getLogger(CreateAccountBalanceHandler::class.java)
    override fun type(): OperationType {
        return OperationType.CREATE_BALANCE
    }

    override fun handle(command: CreateAccountBalanceCommand): AccountBalance {
        log.info("Creating account balance for $command")
        return accountBalanceService.create(command.accountEmail, command.assetSymbol)
    }

    override fun getCommand(): Class<CreateAccountBalanceCommand> {
        return CreateAccountBalanceCommand::class.java
    }
}