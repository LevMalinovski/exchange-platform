package org.exchange.platform.scheduler

import org.exchange.platform.repository.OperationRepository
import org.exchange.platform.service.OperationProcessor
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class OperationsScheduler(
    private val operationRepository: OperationRepository,
    private val operationProcessor: OperationProcessor
) {
    @Transactional
    @Scheduled(fixedDelayString = "\${operations.interval.ms:10000}")
    fun run() {
        processOperations()
    }

    @Transactional
    fun processOperations() {
        val operations = operationRepository.findReadyOperations()
        operations.forEach { operation ->
            try {
                operation.markRunning()
                operationProcessor.processOperation(operation)
                operation.markSuccess()
            } catch (ex: Exception) {
                operation.markFailedOrRetry(ex.message)
            }
            operationRepository.save(operation)
        }
    }
}