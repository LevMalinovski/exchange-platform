package org.exchange.platform.rest.controller

import org.exchange.platform.BasicIntegrationTest
import org.exchange.platform.command.CreateAccountCommand
import org.exchange.platform.repository.AccountRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import java.util.*


class AccountControllerIntTest : BasicIntegrationTest() {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var accountRepository: AccountRepository

    private fun getBaseUrl(): String = "http://localhost:$port/api/v1/account"

    @Test
    fun `should create account and return AccountDto`() {
        val email = "integration_${UUID.randomUUID()}@test.com"
        val command = CreateAccountCommand(email)

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val request = HttpEntity(command, headers)

        val response = restTemplate.postForEntity(getBaseUrl(), request, Map::class.java)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        val body = response.body!!

        assertTrue(body["id"] is String)
        assertEquals(email, body["email"])

        val account = accountRepository.findByEmail(email)
        assertTrue(account.isPresent)
    }
}