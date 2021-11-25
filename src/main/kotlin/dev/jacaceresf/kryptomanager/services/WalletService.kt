package dev.jacaceresf.kryptomanager.services

import dev.jacaceresf.kryptomanager.models.Wallet
import java.math.BigDecimal

interface WalletService {

    fun getWallets(): MutableIterable<Wallet>

    fun getWalletByAddress(address: String): Wallet

    fun createWallet(userEmail: String): Wallet

    fun addFiatBalance(address: String, fiatBalance: BigDecimal)
}