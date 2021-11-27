package dev.jacaceresf.kryptomanager.services

import dev.jacaceresf.kryptomanager.models.Crypto
import dev.jacaceresf.kryptomanager.models.CryptoHistoricInfo

interface CryptoService {

    fun queryPrices(): Collection<Crypto>

    fun queryPricesFromPriceTracker(): Collection<Crypto>

    fun getCryptoInfo(symbol: String): CryptoHistoricInfo

}