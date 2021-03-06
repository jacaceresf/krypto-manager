package dev.jacaceresf.kryptomanager.services

import dev.jacaceresf.kryptomanager.models.req.CryptoTransaction
import dev.jacaceresf.kryptomanager.models.req.CryptoTransactionResponse

interface TransactionService {
    suspend fun doTransaction(cryptoTransaction: CryptoTransaction): CryptoTransactionResponse
}