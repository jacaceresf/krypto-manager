package dev.jacaceresf.kryptomanager.services

import dev.jacaceresf.kryptomanager.models.Crypto
import dev.jacaceresf.kryptomanager.models.CryptoCurrentValue
import dev.jacaceresf.kryptomanager.models.CryptoHistoricInfo

interface CryptoService {

    suspend fun computeAndSavePrices(prices: Collection<CryptoCurrentValue>): Unit

    fun queryPrices(): Collection<Crypto>

    fun queryPricesFromPriceTracker(): Collection<CryptoCurrentValue>

    fun getCryptoInfo(symbol: String): CryptoHistoricInfo

    fun getCryptoBySymbol(symbol: String): Crypto

    fun getCryptoCurrentPrice(symbol: String): CryptoCurrentValue

    fun getCryptoCurrentPriceFromId(cryptoId: Long): CryptoCurrentValue
}