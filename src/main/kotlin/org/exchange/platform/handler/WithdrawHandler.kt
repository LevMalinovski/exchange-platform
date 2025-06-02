package org.exchange.platform.handler


import jakarta.transaction.Transactional
import org.exchange.platform.command.WithdrawCommand
import org.exchange.platform.enum.OperationType
import org.exchange.platform.orchestrator.WithdrawOrchestrator
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class WithdrawHandler(
    private val withdrawOrchestrator: WithdrawOrchestrator
) : Handler<WithdrawCommand> {
    private val log = LoggerFactory.getLogger(WithdrawHandler::class.java)

    override fun type(): OperationType {
        return OperationType.WITHDRAW
    }

    @Transactional
    override fun handle(command: WithdrawCommand) {
        log.info("Starting withdrawal: $command")
        withdrawOrchestrator.withdraw(command)
        log.info("Withdrawal completed: $command")
    }

    override fun getCommand(): Class<WithdrawCommand> {
        return WithdrawCommand::class.java
    }
}