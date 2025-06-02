package org.exchange.platform.repository

import jakarta.persistence.LockModeType
import org.exchange.platform.enum.AssetSymbol
import org.exchange.platform.model.AccountBalance
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AccountBalanceRepository : JpaRepository<AccountBalance, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ab FROM AccountBalance ab WHERE ab.account.id = :accountId AND ab.asset.symbol = :symbol")
    fun lockByAccountIdAndAssetSymbol(
        @Param("accountId") accountId: UUID,
        @Param("symbol") symbol: AssetSymbol
    ): Optional<AccountBalance>

    fun existsAccountBalanceByAccountIdAndAssetId(accountId: UUID, assetId: UUID): Boolean
    fun findByAccountIdAndAssetId(accountId: UUID, assetId: UUID): Optional<AccountBalance>
}