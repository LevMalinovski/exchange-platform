package org.exchange.platform.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.exchange.platform.command.Command
import org.exchange.platform.enum.OperationType
import org.exchange.platform.model.Operation
import org.exchange.platform.repository.OperationRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class OperationService(private val operationRepository: OperationRepository) {
    private val log = LoggerFactory.getLogger(OperationService::class.java)
    val mapper = jacksonObjectMapper()

    @Async
    fun add(type: OperationType, command: Command) {
        try {
            val payload = mapper.writeValueAsString(command)
            val operation = Operation(type = type, payload = payload)
            operationRepository.save(operation)
        } catch (e: Exception) {
            log.error("Exception occurred when saving operation", e)
            // Inform customer that operation cannot proceed
        }
    }
}