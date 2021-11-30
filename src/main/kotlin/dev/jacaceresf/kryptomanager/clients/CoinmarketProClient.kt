package dev.jacaceresf.kryptomanager.clients

import dev.jacaceresf.kryptomanager.constants.ApiConstants
import dev.jacaceresf.kryptomanager.models.coinmarketcap.CoinmarketCapResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import javax.annotation.PostConstruct


@Component
class CoinmarketProClient {

    val log: Logger = LoggerFactory.getLogger(this.javaClass.name)

    @Value("\${coinmarket.api_key}")
    private val coinmarketCapApiKey: String = ""

    @Value("\${coinmarket.base_url}")
    private val coinmarketCapBaseUrl: String = ""

    private lateinit var webClient: WebClient

    @PostConstruct
    fun init() {
        log.info("Going to build web client with base URL -> [$coinmarketCapBaseUrl]")
        webClient = WebClient.builder()
            .baseUrl(coinmarketCapBaseUrl)
            .defaultHeader(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE)
            .defaultHeader(ApiConstants.API_KEY_HEADER_NAME, coinmarketCapApiKey)
            .build()

//        HttpClient httpClient = HttpClient.create()
//            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
//            .responseTimeout(Duration.ofMillis(5000))
//            .doOnConnected(conn ->
//        conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
//        .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));
//
//        WebClient client = WebClient.builder()
//            .clientConnector(new ReactorClientHttpConnector(httpClient))
//            .build();
    }


    fun getCryptoData(cryptos: Collection<String>): CoinmarketCapResponse {

        log.info("Going to retrieve crypto prices from CoinmarketCap")

        if (cryptos.isEmpty()) {
            throw RuntimeException("Crypto symbols can't be null.")
        }

        val cryptoSymbols = cryptos.joinToString(separator = ",")

        val responseEntity = webClient.get()
            .uri("/quotes/latest?symbol=$cryptoSymbols")
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError) {
                Mono.error<RuntimeException>(
                    RuntimeException(
                        "${it.statusCode()}"
                    )
                )
            }
            .toEntity(CoinmarketCapResponse::class.java)
            .block()

        responseEntity?.let {
            log.info("Response [${it.statusCode}]")
            return it.body ?: throw java.lang.RuntimeException("Response body is null")
        }

        throw RuntimeException("Response is null.")
    }
}