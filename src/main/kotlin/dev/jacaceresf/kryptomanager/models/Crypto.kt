package dev.jacaceresf.kryptomanager.models

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Table(value = "cryptocurrency")
data class Crypto(
    @Id
    @JsonIgnore
    val id: Long? = null,
    val symbol: String,
    val name: String
) {
    constructor() : this(symbol = "", name = "") {

    }
}

@Table
data class CryptoValue(
    @Id
    @JsonIgnore
    val id: Long? = null,
    val cryptoId: Long,
    val time: LocalDateTime,
    val value: BigDecimal
) {
    constructor() : this(cryptoId = -1, time = LocalDateTime.now(), value = BigDecimal.ZERO) {

    }
}

data class CryptoHistoricInfo(
    val crypto: Crypto,
    val historicValues: Collection<CryptoValue>
)

data class CryptoCurrentValue(
    val symbol: String,
    val name: String,
    val currentPrice: BigDecimal
)