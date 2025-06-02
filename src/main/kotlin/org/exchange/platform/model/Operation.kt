package org.exchange.platform.model

import jakarta.persistence.*
import org.exchange.platform.enum.OperationStatus
import org.exchange.platform.enum.OperationType
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.Instant
import java.util.*

@Entity
@Table(name = "operations")
data class Operation(
    @Id
    val id: UUID = UUID.randomUUID(),

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: OperationType,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    val payload: String, // serialized command as JSON

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: OperationStatus = OperationStatus.PENDING,

    @Column(name = "failed_reason")
    var failedReason: String? = null,

    @Column(name = "retry_count", nullable = false)
    var retryCount: Int = 0,

    @Column(name = "next_retry_at")
    var nextRetryAt: Instant? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant = Instant.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant = Instant.now()
) {
    fun markSuccess() {
        status = OperationStatus.SUCCESS
        updatedAt = Instant.now()
    }

    fun markRunning() {
        status = OperationStatus.RUNNING
        updatedAt = Instant.now()
    }

    fun markFailedOrRetry(error: String?, maxRetries: Int = 5) {
        retryCount++
        updatedAt = Instant.now()

        if (retryCount >= maxRetries) {
            status = OperationStatus.FAILED
            failedReason = error
        } else {
            status = OperationStatus.RETRYING
            nextRetryAt = Instant.now().plusSeconds(calculateBackoffDelay(retryCount))
        }
    }

    private fun calculateBackoffDelay(attempt: Int): Long {
        // Exponential backoff (e.g., 2^attempt * 10 seconds)
        return Math.pow(2.0, attempt.toDouble()).toLong() * 10
    }
}