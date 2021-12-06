package dev.jacaceresf.kryptomanager.repositories

import dev.jacaceresf.kryptomanager.models.Wallet
import dev.jacaceresf.kryptomanager.models.WalletMovement
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface WalletRepository : ReactiveCrudRepository<Wallet, Long> {

    suspend fun findByAddress(address: String): Mono<Wallet>

    suspend fun findByUserEmail(userEmail: String): Mono<Wallet>
}

@Repository
interface WalletMovementRepository : ReactiveCrudRepository<WalletMovement, Long> {

    suspend fun findByWalletId(walletId: Long): Flux<WalletMovement>

}