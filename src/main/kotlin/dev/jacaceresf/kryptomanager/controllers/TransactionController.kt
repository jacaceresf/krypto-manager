package dev.jacaceresf.kryptomanager.controllers

import dev.jacaceresf.kryptomanager.models.req.CryptoTransaction
import dev.jacaceresf.kryptomanager.models.req.CryptoTransactionResponse
import dev.jacaceresf.kryptomanager.services.TransactionService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/transactions")
class TransactionController(private val transactionService: TransactionService) {

    @PostMapping("/new")
    suspend fun doTransaction(@RequestBody cryptoTransaction: CryptoTransaction): CryptoTransactionResponse =
        transactionService.doTransaction(cryptoTransaction)
}