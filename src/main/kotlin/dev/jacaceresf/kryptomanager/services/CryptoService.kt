package dev.jacaceresf.kryptomanager.services

import dev.jacaceresf.kryptomanager.models.Crypto
import dev.jacaceresf.kryptomanager.models.CryptoCurrentValue
import dev.jacaceresf.kryptomanager.models.CryptoHistoricInfo

interface CryptoService {

    suspend fun computeAndSavePrices(prices: Collection<CryptoCurrentValue>): Unit

    suspend fun queryPrices(): Collection<Crypto>

    suspend fun queryPricesFromPriceTracker(): Collection<CryptoCurrentValue>

    suspend fun getCryptoInfo(symbol: String): CryptoHistoricInfo

    suspend fun getCryptoBySymbol(symbol: String): Crypto

    suspend fun getCryptoCurrentPrice(symbol: String): CryptoCurrentValue

    suspend fun getCryptoCurrentPriceFromId(cryptoId: Long): CryptoCurrentValue
}