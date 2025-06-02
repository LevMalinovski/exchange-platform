package org.exchange.platform.service

import org.exchange.platform.model.Account
import org.exchange.platform.repository.AccountRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*
import javax.security.auth.login.AccountException

@Service
class AccountService(private val accountRepository: AccountRepository) {
    private val log = LoggerFactory.getLogger(AccountService::class.java)

    fun getAccount(email: String): Account {
        return accountRepository.findByEmail(email).orElseThrow({
            log.warn("Account not found for email: $email")
            AccountException("Account not found")
        })
    }

    fun getAccount(accountId: UUID): Account {
        return accountRepository.findById(accountId).orElseThrow({
            log.warn("Account not found for uuid: $accountId")
            AccountException("Account not found")
        })
    }
}