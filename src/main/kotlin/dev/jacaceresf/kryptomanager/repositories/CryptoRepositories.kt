package dev.jacaceresf.kryptomanager.repositories

import dev.jacaceresf.kryptomanager.models.Crypto
import dev.jacaceresf.kryptomanager.models.CryptoValue
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface CryptoRepository : ReactiveCrudRepository<Crypto, Long> {
    fun findBySymbol(symbol: String): Mono<Crypto>
}

@Repository
interface CryptoValueRepository : ReactiveCrudRepository<CryptoValue, Long>