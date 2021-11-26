package dev.jacaceresf.kryptomanager.models.req

import java.math.BigDecimal

data class WalletFiatReq(val walletAddress: String, val balance: BigDecimal)