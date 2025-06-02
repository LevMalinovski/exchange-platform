package org.exchange.platform.repository

import org.exchange.platform.model.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AccountRepository : JpaRepository<Account, UUID> {
    fun findByEmail(email: String): Optional<Account>
    fun existsByEmail(email: String): Boolean
}