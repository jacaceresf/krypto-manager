package dev.jacaceresf.kryptomanager.services

import dev.jacaceresf.kryptomanager.clients.CoinmarketProClient
import dev.jacaceresf.kryptomanager.exceptions.NotFoundException
import dev.jacaceresf.kryptomanager.models.Crypto
import dev.jacaceresf.kryptomanager.models.CryptoCurrentValue
import dev.jacaceresf.kryptomanager.models.CryptoHistoricInfo
import dev.jacaceresf.kryptomanager.models.CryptoValue
import dev.jacaceresf.kryptomanager.repositories.CryptoRepository
import dev.jacaceresf.kryptomanager.repositories.CryptoValueRepository
import dev.jacaceresf.kryptomanager.utils.CryptoUtils
import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class CryptoServiceImpl(
    private val cryptoRepository: CryptoRepository,
    private val cryptoValueRepository: CryptoValueRepository,
    private val coinmarketProClient: CoinmarketProClient
) : CryptoService {

    private val log = LoggerFactory.getLogger(this.javaClass.name)

    override suspend fun computeAndSavePrices(prices: Collection<CryptoCurrentValue>) = coroutineScope {
        val now = LocalDateTime.now()
        prices.forEach {
            Thread.sleep(2000)
            val crypto = cryptoRepository.findBySymbol(it.symbol)
            val cryptoValue = CryptoValue(
                cryptoId = crypto.get().id, time = now, value = it.currentPrice
            )
            log.info("Going to save $cryptoValue at ${LocalDateTime.now()}")
            cryptoValueRepository.save(cryptoValue)
        }
    }


    override suspend fun queryPrices(): Collection<Crypto> {
        TODO("Not yet implemented")
    }

    override suspend fun queryPricesFromPriceTracker(): Collection<CryptoCurrentValue> {

        val cryptosDb = cryptoRepository.findAll()

        val cryptoSymbol = mutableListOf<String>()
        cryptosDb.forEach { cryptoSymbol.add(it.symbol) }

        val cryptoData = coinmarketProClient.getCryptoData(cryptoSymbol)

        val result = mutableListOf<CryptoCurrentValue>()
        for ((symbol, value) in cryptoData.data) {

            result.add(
                CryptoCurrentValue(
                    symbol = symbol,
                    name = value.name,
                    currentPrice = BigDecimal.valueOf(value.quote["USD"]?.price!!)
                )
            )
        }

        computeAndSavePrices(result)

        log.info("Returning result with ${result.count()} items")
        return result
    }

    override suspend fun getCryptoInfo(symbol: String): CryptoHistoricInfo {

        val crypto = CryptoUtils.getCryptoFromOptional(cryptoRepository.findBySymbol(symbol))

        val priceDb = cryptoValueRepository.findAll().toList()

        val prices = if (priceDb.isNotEmpty()) priceDb.toList() else emptyList()

        return CryptoHistoricInfo(
            crypto = crypto,
            historicValues = prices
        )
    }

    override suspend fun getCryptoBySymbol(symbol: String): Crypto {
        return CryptoUtils.getCryptoFromOptional(cryptoRepository.findBySymbol(symbol))
    }

    override suspend fun getCryptoCurrentPrice(symbol: String): CryptoCurrentValue {

        val cryptoData = coinmarketProClient.getCryptoData(mutableListOf(symbol))

        val coinmarketValue =
            cryptoData.data.getOrDefault(symbol, null) ?: throw NotFoundException("Crypto with symbol not found.")

        val cryptoCurrentValue = CryptoCurrentValue(
            symbol = symbol,
            name = coinmarketValue.name,
            currentPrice = BigDecimal.valueOf(coinmarketValue.quote["USD"]?.price!!)
        )

        computeAndSavePrices(mutableListOf(cryptoCurrentValue))

        return cryptoCurrentValue
    }

    override suspend fun getCryptoCurrentPriceFromId(cryptoId: Long): CryptoCurrentValue {

        val crypto = CryptoUtils.getCryptoFromOptional(cryptoRepository.findById(cryptoId))

        return getCryptoCurrentPrice(crypto.symbol)
    }
}