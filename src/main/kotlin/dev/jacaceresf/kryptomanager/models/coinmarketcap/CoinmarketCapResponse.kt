package dev.jacaceresf.kryptomanager.models.coinmarketcap

data class CoinmarketCapResponse(
    val data: Map<String, CoinmarketCryptoValue>,
    val status: Status
)