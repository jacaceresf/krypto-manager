package dev.jacaceresf.kryptomanager.models

import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Transaction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = -1,
    val token: String,
    val walletId: Long = -1,
    val cryptoId: Long = -1,
    val timestamp: LocalDateTime,
    val amount: BigDecimal,
    val executionPrice: BigDecimal,
    val cryptoPrice: BigDecimal,
    val type: TransactionType
) {
    constructor() : this(
        token = "",
        timestamp = LocalDateTime.now(),
        amount = BigDecimal.ZERO,
        executionPrice = BigDecimal.ZERO,
        cryptoPrice = BigDecimal.ZERO,
        type = TransactionType.BUY
    ) {

    }

}

enum class TransactionType {
    BUY, SELL
}