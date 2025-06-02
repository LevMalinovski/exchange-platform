package org.exchange.platform.enum

enum class OperationType {
    CREATE_BALANCE, DEPOSIT, WITHDRAW, BUY, SELL
}

enum class OperationStatus {
    PENDING, RUNNING, RETRYING, SUCCESS, FAILED
}