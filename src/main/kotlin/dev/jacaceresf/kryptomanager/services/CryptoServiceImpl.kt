package dev.jacaceresf.kryptomanager.services

import dev.jacaceresf.kryptomanager.models.Crypto
import dev.jacaceresf.kryptomanager.models.CryptoHistoricInfo
import dev.jacaceresf.kryptomanager.repositories.CryptoRepository
import dev.jacaceresf.kryptomanager.repositories.CryptoValueRepository
import dev.jacaceresf.kryptomanager.utils.CryptoUtils
import org.springframework.stereotype.Service

@Service
class CryptoServiceImpl(
    private val cryptoRepository: CryptoRepository,
    private val cryptoValueRepository: CryptoValueRepository
) : CryptoService {


    override fun queryPrices(): Collection<Crypto> {
        TODO("Not yet implemented")
    }

    override fun queryPricesFromPriceTracker(): Collection<Crypto> {
//        val cryptosDb = cryptoRepository.findAll()
//
//        val cryptoFromClient = null
//
//
//
//        cryptosDb.forEach {
//
//        }

        return emptyList()
    }

    override fun getCryptoInfo(symbol: String): CryptoHistoricInfo {

        val crypto = CryptoUtils.getCryptoFromOptional(cryptoRepository.findBySymbol(symbol))

        val priceDb = cryptoValueRepository.findAll()

        val prices = if (priceDb.count() > 0) {
            priceDb.toList()
        } else {
            emptyList()
        }

        return CryptoHistoricInfo(
            crypto = crypto,
            historicValues = prices
        )
    }
}