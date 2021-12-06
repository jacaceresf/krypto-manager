package dev.jacaceresf.kryptomanager.models

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDateTime


@Table
data class Wallet(
    @JsonIgnore
    @Id
    val id: Long? = null,
    val userEmail: String,
    val address: String,
    val creationDate: LocalDateTime,
    var balance: BigDecimal
) {
    constructor() : this(userEmail = "", address = "", creationDate = LocalDateTime.now(), balance = BigDecimal.ZERO) {

    }

}