package org.exchange.platform.advice

import org.exchange.platform.exception.AppException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationErrors(ex: MethodArgumentNotValidException): ResponseEntity<Any?> {
        val errors: Map<String, String> = ex.bindingResult.fieldErrors
            .associate { fieldError -> fieldError.field to (fieldError.defaultMessage ?: "Invalid value") }
        return ResponseEntity<Any?>(
            mapOf("errors" to errors),
            org.springframework.http.HttpHeaders(),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(AppException::class)
    fun handleValidationErrors(ex: AppException): ResponseEntity<Any?> {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(mapOf("error" to ex.message.orEmpty()))
    }

    // Catch all other exceptions
    @ExceptionHandler(Exception::class)
    fun handleGeneralException(ex: Exception): ResponseEntity<Any?> {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(mapOf("error" to ex.message.orEmpty()))
    }
}