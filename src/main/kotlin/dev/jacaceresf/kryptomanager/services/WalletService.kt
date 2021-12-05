package dev.jacaceresf.kryptomanager.services

import dev.jacaceresf.kryptomanager.models.Transaction
import dev.jacaceresf.kryptomanager.models.Wallet
import dev.jacaceresf.kryptomanager.models.WalletMovementDetail
import dev.jacaceresf.kryptomanager.models.req.CryptoTransactionResponse
import dev.jacaceresf.kryptomanager.models.req.WalletFiatReq

interface WalletService {

    suspend fun getWallets(): MutableIterable<Wallet>

    suspend fun getWalletByAddress(address: String): Wallet

    suspend fun createWallet(userEmail: String): Wallet

    suspend fun addFiatBalance(walletFiatReq: WalletFiatReq): Wallet

    suspend fun getWalletMovements(address: String, from: String?, to: String?): WalletMovementDetail

    suspend fun updateWalletBalanceFromCryptoTransaction(cryptoTransactionResponse: CryptoTransactionResponse)

    suspend fun saveWalletMovementFromTransaction(cryptoTransactionResponse: CryptoTransactionResponse)

    suspend fun getWalletTransactions(walletId: Long): Collection<Transaction>
}