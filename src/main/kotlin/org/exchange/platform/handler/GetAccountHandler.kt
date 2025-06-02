package org.exchange.platform.handler

import org.exchange.platform.command.GetAccountQuery
import org.exchange.platform.model.Account
import org.exchange.platform.service.AccountService
import org.springframework.stereotype.Service

@Service
class GetAccountHandler(private val accountService: AccountService) {
    fun handle(query: GetAccountQuery): Account {
        return accountService.getAccount(query.email);
    }
}