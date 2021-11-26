package dev.jacaceresf.kryptomanager.models

import com.fasterxml.jackson.annotation.JsonIgnore
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class WalletMovement(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore val id: Long = -1,
    val movementId: String,
    val balance: BigDecimal,
    @JsonIgnore val walletAddress: String,
    val movementType: MovementType,
    val timestamp: LocalDateTime
) {
    constructor() : this(
        movementId = "",
        balance = BigDecimal.ZERO,
        walletAddress = "",
        movementType = MovementType.DEBIT,
        timestamp = LocalDateTime.now()
    ) {

    }
}

enum class MovementType {
    DEBIT, CREDIT
}

data class WalletMovementDetail(val wallet: Wallet, val movements: Collection<WalletMovement>)