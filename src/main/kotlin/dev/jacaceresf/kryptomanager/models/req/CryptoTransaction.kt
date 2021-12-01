package dev.jacaceresf.kryptomanager.models.req

import dev.jacaceresf.kryptomanager.models.Crypto
import dev.jacaceresf.kryptomanager.models.Wallet
import java.math.BigDecimal

data class CryptoTransaction(
    val walletAddress: String,
    val crypto: String,
    val amount: BigDecimal,
    val type: CryptoTransactionType
)

enum class CryptoTransactionType {
    BUY, SELL
}

data class CryptoTransactionResponse(
    val wallet: Wallet,
    val crypto: Crypto,
    val executionPrice: BigDecimal,
    val amount: BigDecimal,
    val type: CryptoTransactionType
)