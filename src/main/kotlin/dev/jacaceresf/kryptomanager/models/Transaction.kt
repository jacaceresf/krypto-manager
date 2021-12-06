package dev.jacaceresf.kryptomanager.models

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Table
data class Transaction(
    @Id
    val id: Long? = null,
    val token: String,
    val walletId: Long? = null,
    val cryptoId: Long? = null,
    val timestamp: LocalDateTime,
    val amount: BigDecimal,
    val executionPrice: BigDecimal,
    val cryptoPrice: BigDecimal,
    @Enumerated(EnumType.STRING)
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