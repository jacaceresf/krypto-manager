package dev.jacaceresf.kryptomanager.models.coinmarketcap

import java.math.BigDecimal

data class CoinmarketCryptoValue(
    val circulating_supply: BigDecimal,
    val cmc_rank: BigDecimal,
    val date_added: String,
    val id: BigDecimal,
    val is_active: BigDecimal,
    val is_fiat: BigDecimal,
    val last_updated: String,
    val max_supply: BigDecimal?,
    val name: String,
    val num_market_pairs: BigDecimal,
    val quote: Map<String, FiatValue>,
    val slug: String,
    val symbol: String,
    val tags: List<String>,
    val total_supply: BigDecimal
)