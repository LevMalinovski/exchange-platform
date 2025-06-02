package org.exchange.platform.rest.dto

import org.exchange.platform.model.Account
import java.util.*

data class AccountResponseDto(
    val id: UUID,
    val email: String,
    val balances: List<AccountBalanceResponseDto>? = null,
) {
    companion object {
        fun toDto(account: Account): AccountResponseDto {
            val balances = account.balances.map { balance ->
                AccountBalanceResponseDto(
                    asset = AssetResponseDto(
                        symbol = balance.asset.symbol.name,
                        name = balance.asset.name,
                        type = balance.asset.type.name
                    ),
                    balance = balance.amount
                )
            }
            return AccountResponseDto(account.id, account.email, balances)
        }
    }
}