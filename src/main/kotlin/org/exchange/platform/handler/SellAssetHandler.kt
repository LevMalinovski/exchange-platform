package org.exchange.platform.handler


import jakarta.transaction.Transactional
import org.exchange.platform.command.SellAssetCommand
import org.exchange.platform.enum.OperationType
import org.exchange.platform.orchestrator.SellAssetOrchestrator
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SellAssetHandler(
    private val sellAssetOrchestrator: SellAssetOrchestrator
) : Handler<SellAssetCommand> {
    private val log = LoggerFactory.getLogger(SellAssetHandler::class.java)

    override fun type(): OperationType {
        return OperationType.SELL
    }

    @Transactional
    override fun handle(command: SellAssetCommand) {
        log.info("Starting sell operation: $command")
        sellAssetOrchestrator.handle(command)
        log.info("Sell operation completed: $command")
    }

    override fun getCommand(): Class<SellAssetCommand> {
        return SellAssetCommand::class.java
    }
}