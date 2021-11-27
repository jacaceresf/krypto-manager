package dev.jacaceresf.kryptomanager.models

import com.fasterxml.jackson.annotation.JsonIgnore
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name = "cryptocurrency")
data class Crypto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    val id: Long = -1,
    val symbol: String,
    val name: String
) {
    constructor() : this(symbol = "", name = "") {

    }
}

@Entity
data class CryptoValue(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    val id: Long = -1,
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