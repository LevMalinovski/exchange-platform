package org.exchange.platform.rest.controller

import org.exchange.platform.command.CreateAccountCommand
import org.exchange.platform.command.GetAccountQuery
import org.exchange.platform.handler.CreateAccountHandler
import org.exchange.platform.handler.GetAccountHandler
import org.exchange.platform.model.Account
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.*


class AccountControllerUnitTest {
    private val createAccountHandler: CreateAccountHandler = mock()
    private val getAccountHandler: GetAccountHandler = mock()
    private val controller = AccountController(createAccountHandler, getAccountHandler)

    @Test
    fun `should create account and return response`() {
        val email = "unit@test.com"
        val command = CreateAccountCommand(email)
        val account = Account(id = UUID.randomUUID(), email = email)

        `when`(createAccountHandler.handle(command)).thenReturn(account)
        // When
        val response = controller.createAccount(command)
        // Then
        assertEquals(account.id, response.body!!.id)
        assertEquals(account.email, response.body!!.email)
        verify(createAccountHandler).handle(command)
    }

    @Test
    fun `should get account and return response`() {
        // Given
        val email = "unit@test.com"
        val query = GetAccountQuery(email)
        val account = Account(id = UUID.randomUUID(), email = email)

        `when`(getAccountHandler.handle(query)).thenReturn(account)
        // When
        val response = controller.getAccount(email)
        // Then
        assertEquals(account.id, response.body!!.id)
        assertEquals(account.email, response.body!!.email)
        verify(getAccountHandler).handle(query)
    }
}