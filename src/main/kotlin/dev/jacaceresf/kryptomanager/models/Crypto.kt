package dev.jacaceresf.kryptomanager.models

import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Crypto(
    @Id val symbol: String,
    val name: String
) {
    constructor() : this("", "") {

    }
}

@Entity
data class CryptoValue(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Long = -1,
    val symbol: String,
    val time: LocalDateTime,
    val value: BigDecimal
) {
    constructor() : this(symbol = "", time = LocalDateTime.now(), value = BigDecimal.ZERO) {

    }
}
