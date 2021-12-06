package dev.jacaceresf.kryptomanager.models

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Table
data class WalletMovement(
    @Id
    @JsonIgnore val id: Long? = null,
    val movementId: String,
    val balance: BigDecimal,
    @JsonIgnore
    val walletId: Long,
    @Enumerated(EnumType.STRING)
    val movementType: MovementType,
    val timestamp: LocalDateTime,
    val description: String
) {
    constructor() : this(
        movementId = "",
        balance = BigDecimal.ZERO,
        walletId = -1,
        movementType = MovementType.DEBIT,
        timestamp = LocalDateTime.now(),
        description = ""
    ) {

    }
}

enum class MovementType {
    DEBIT, CREDIT
}

data class WalletMovementDetail(val wallet: Wallet, val movements: Collection<WalletMovement>)