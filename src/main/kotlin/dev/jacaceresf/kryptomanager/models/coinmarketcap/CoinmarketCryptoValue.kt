package dev.jacaceresf.kryptomanager.models.coinmarketcap

data class CoinmarketCryptoValue(
    val circulating_supply: Int,
    val cmc_rank: Int,
    val date_added: String,
    val id: Int,
    val is_active: Int,
    val is_fiat: Int,
    val last_updated: String,
    val max_supply: Int,
    val name: String,
    val num_market_pairs: Int,
    val platform: Any,
    val quote: Map<String, FiatValue>,
    val slug: String,
    val symbol: String,
    val tags: List<String>,
    val total_supply: Int
)