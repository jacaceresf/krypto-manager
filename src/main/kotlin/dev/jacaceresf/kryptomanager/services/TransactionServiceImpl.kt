package dev.jacaceresf.kryptomanager.services

import dev.jacaceresf.kryptomanager.models.Transaction
import dev.jacaceresf.kryptomanager.models.TransactionType
import dev.jacaceresf.kryptomanager.models.req.CryptoTransaction
import dev.jacaceresf.kryptomanager.models.req.CryptoTransactionResponse
import dev.jacaceresf.kryptomanager.models.req.CryptoTransactionType
import dev.jacaceresf.kryptomanager.repositories.TransactionRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.time.LocalDateTime

@Service
class TransactionServiceImpl(
    private val cryptoService: CryptoService,
    private val walletService: WalletService,
    private val transactionRepository: TransactionRepository,
    private val walletBalanceService: WalletBalanceService
) :
    TransactionService {

    val log: Logger = LoggerFactory.getLogger(this.javaClass.name)

    companion object Util {

        private val MD5_ENCODER: MessageDigest = MessageDigest.getInstance("MD5")

        fun getTransactionToken(walletAddress: String, cryptoSymbol: String): String {

            val token = walletAddress + cryptoSymbol + System.currentTimeMillis()

            return MD5_ENCODER.digest(token.toByteArray())
                .joinToString("") { "%02x".format(it) }
        }
    }

    override suspend fun doTransaction(cryptoTransaction: CryptoTransaction): CryptoTransactionResponse {

        log.info("Going to create transaction with request -> $cryptoTransaction")

        /// get wallet
        val wallet = walletService.getWalletByAddress(cryptoTransaction.walletAddress)

        ///get crypto from symbol
        val crypto = cryptoService.getCryptoBySymbol(cryptoTransaction.crypto)

        ///get crypto price
        val price = cryptoService.getCryptoCurrentPrice(crypto.symbol)

        val executionPrice = price.currentPrice * cryptoTransaction.amount

        val txType: TransactionType
        if (cryptoTransaction.type == CryptoTransactionType.BUY) {
            txType = TransactionType.BUY

            ///check amount with balance
            if (wallet.balance < executionPrice) {
                throw RuntimeException("Insufficient wallet balance.")
            }

        } else {
            txType = TransactionType.SELL
            val walletCompositeBalance = walletBalanceService.getWalletCompositeBalance(wallet.address)

            if (walletCompositeBalance.cryptoBalance.isEmpty()) {
                throw RuntimeException("You don't own any kind of crypto so you can't sell.")
            }

            val cryptoBalance =
                walletCompositeBalance.cryptoBalance.firstOrNull { it.crypto.symbol == cryptoTransaction.crypto }
                    ?: throw RuntimeException("You don't have ${cryptoTransaction.crypto} available")

            if (cryptoBalance.balance < cryptoTransaction.amount) {
                throw RuntimeException("You don't have enough balance for current operation.")
            }
        }

        ///save transaction
        val transactionToSave = Transaction(
            token = getTransactionToken(wallet.address, crypto.symbol),
            walletId = wallet.id,
            cryptoId = crypto.id,
            timestamp = LocalDateTime.now(),
            amount = cryptoTransaction.amount,
            executionPrice = executionPrice,
            cryptoPrice = price.currentPrice,
            type = txType
        )

        log.info("Got transaction token -> [${transactionToSave.token}]")

        transactionRepository.save(transactionToSave)

        val response = CryptoTransactionResponse(
            wallet = wallet,
            crypto = crypto,
            executionPrice = price.currentPrice,
            amount = cryptoTransaction.amount,
            type = cryptoTransaction.type
        )

        ///update wallet balance
        walletService.updateWalletBalanceFromCryptoTransaction(response)

        ///return response
        return response
    }
}