package org.exchange.platform.handler

import org.exchange.platform.command.CreateAccountCommand
import org.exchange.platform.model.Account
import org.exchange.platform.repository.AccountRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.security.auth.login.AccountException

@Service
class CreateAccountHandler(private val accountRepository: AccountRepository) {
    private val log = LoggerFactory.getLogger(CreateAccountHandler::class.java)

    fun handle(command: CreateAccountCommand): Account {
        log.info("Creating account for $command")
        if (accountRepository.existsByEmail(command.email)) {
            throw AccountException("Account not exists")
        }
        val account = Account(email = command.email)
        accountRepository.save(account)
        log.info("Account created for $command")
        return account
    }
}