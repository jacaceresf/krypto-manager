package dev.jacaceresf.kryptomanager.repositories

import dev.jacaceresf.kryptomanager.models.Transaction
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface TransactionRepository : ReactiveCrudRepository<Transaction, Long> {
    fun findByWalletIdOrderByTimestampAsc(walletId: Long): Flux<Transaction>
}