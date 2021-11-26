package dev.jacaceresf.kryptomanager.services

import dev.jacaceresf.kryptomanager.models.Wallet
import dev.jacaceresf.kryptomanager.models.WalletMovementDetail
import dev.jacaceresf.kryptomanager.models.req.WalletFiatReq

interface WalletService {

    fun getWallets(): MutableIterable<Wallet>

    fun getWalletByAddress(address: String): Wallet

    fun createWallet(userEmail: String): Wallet

    fun addFiatBalance(walletFiatReq: WalletFiatReq): Wallet

    fun getWalletMovements(address: String, from: String?, to: String?): WalletMovementDetail
}