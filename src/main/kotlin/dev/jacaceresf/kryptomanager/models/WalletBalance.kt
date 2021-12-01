package dev.jacaceresf.kryptomanager.models

import java.math.BigDecimal


data class WalletCompositeBalance(
    val wallet: Wallet,
    val cryptoBalance: Collection<CryptoBalance>,
    val currentCryptoValues: BigDecimal
)

data class CryptoBalance(val crypto: Crypto, val balance: BigDecimal, val currentValue: BigDecimal)
