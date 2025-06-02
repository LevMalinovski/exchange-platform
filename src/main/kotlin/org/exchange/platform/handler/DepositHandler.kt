package org.exchange.platform.handler


import jakarta.transaction.Transactional
import org.exchange.platform.command.DepositCommand
import org.exchange.platform.enum.OperationType
import org.exchange.platform.orchestrator.DepositOrchestrator
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DepositHandler(
    private val depositOrchestrator: DepositOrchestrator
) : Handler<DepositCommand> {
    private val log = LoggerFactory.getLogger(DepositHandler::class.java)

    override fun type(): OperationType {
        return OperationType.DEPOSIT
    }

    @Transactional
    override fun handle(command: DepositCommand) {
        log.info("Starting deposit: $command")
        depositOrchestrator.deposit(command)
        log.info("Deposit completed: $command")
    }

    override fun getCommand(): Class<DepositCommand> {
        return DepositCommand::class.java
    }
}