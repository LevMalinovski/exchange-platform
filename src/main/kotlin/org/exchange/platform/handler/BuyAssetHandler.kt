package org.exchange.platform.handler


import jakarta.transaction.Transactional
import org.exchange.platform.command.BuyAssetCommand
import org.exchange.platform.enum.OperationType
import org.exchange.platform.orchestrator.BuyAssetOrchestrator
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class BuyAssetHandler(
    private val buyAssetOrchestrator: BuyAssetOrchestrator
) : Handler<BuyAssetCommand> {
    private val log = LoggerFactory.getLogger(BuyAssetHandler::class.java)

    override fun type(): OperationType {
        return OperationType.BUY
    }

    @Transactional
    override fun handle(command: BuyAssetCommand) {
        log.info("Starting buy operation: $command")
        buyAssetOrchestrator.handle(command)
        log.info("Buy operation completed: $command")
    }

    override fun getCommand(): Class<BuyAssetCommand> {
        return BuyAssetCommand::class.java
    }
}