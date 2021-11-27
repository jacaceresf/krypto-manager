package dev.jacaceresf.kryptomanager.repositories

import dev.jacaceresf.kryptomanager.models.Crypto
import dev.jacaceresf.kryptomanager.models.CryptoValue
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CryptoRepository : CrudRepository<Crypto, Long> {
    fun findBySymbol(symbol: String): Optional<Crypto>
}

@Repository
interface CryptoValueRepository : CrudRepository<CryptoValue, Long>