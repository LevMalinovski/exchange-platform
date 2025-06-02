package org.exchange.platform.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.exchange.platform.command.Command
import org.exchange.platform.exception.AppException
import org.exchange.platform.handler.Handler
import org.exchange.platform.model.Operation
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional


@Component
@Transactional
class OperationProcessor(
    @Autowired
    private val handlers: MutableList<Handler<out Command>?>?
) {
    private val log = LoggerFactory.getLogger(OperationProcessor::class.java)
    val mapper = jacksonObjectMapper()

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun processOperation(op: Operation) {
        log.info("Processing ${op.type}")
        val handler = findHandler(op.type.name)
        if (handler == null) {
            log.error("Handler for ${op.type} not found")
            throw AppException("Handler for ${op.type} not found")
        }
        dispatch(op, handler)
        log.info("Completed processing ${op.type}")
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Command> dispatch(op: Operation, handler: Handler<T>) {
        val command = mapper.readValue(op.payload, handler.getCommand()) as T
        handler.handle(command)
    }

    fun findHandler(type: String): Handler<out Command>? {
        return handlers?.firstOrNull { it?.type()?.name == type }
    }
}