package dev.jacaceresf.kryptomanager.repositories

import dev.jacaceresf.kryptomanager.models.Transaction
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository : ReactiveCrudRepository<Transaction, Long> {
    fun findByWalletIdOrderByTimestampAsc(walletId: Long): Flow<Transaction>
}