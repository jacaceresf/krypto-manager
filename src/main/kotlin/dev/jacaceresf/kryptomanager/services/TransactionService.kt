package dev.jacaceresf.kryptomanager.services

import dev.jacaceresf.kryptomanager.models.Transaction
import dev.jacaceresf.kryptomanager.models.req.CryptoTransaction
import dev.jacaceresf.kryptomanager.models.req.CryptoTransactionResponse

interface TransactionService {

    fun doTransaction(cryptoTransaction: CryptoTransaction): CryptoTransactionResponse

    fun getWalletTransactions(walletId: Long): Collection<Transaction>
}