package org.exchange.platform.repository

import jakarta.persistence.LockModeType
import jakarta.persistence.QueryHint
import org.exchange.platform.model.Operation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.QueryHints
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface OperationRepository : JpaRepository<Operation, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(QueryHint(name = "javax.persistence.lock.timeout", value = "0"))
    @Query(
        """
        SELECT po FROM Operation po
        WHERE po.status IN ('PENDING', 'RETRYING')
          AND (po.nextRetryAt IS NULL OR po.nextRetryAt <= CURRENT_TIMESTAMP)
        ORDER BY po.createdAt DESC
    """
    )
    fun findReadyOperations(): List<Operation>
}