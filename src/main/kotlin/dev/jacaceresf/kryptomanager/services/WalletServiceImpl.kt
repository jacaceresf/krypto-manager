package dev.jacaceresf.kryptomanager.services

import dev.jacaceresf.kryptomanager.models.*
import dev.jacaceresf.kryptomanager.models.req.CryptoTransactionResponse
import dev.jacaceresf.kryptomanager.models.req.CryptoTransactionType
import dev.jacaceresf.kryptomanager.models.req.WalletFiatReq
import dev.jacaceresf.kryptomanager.repositories.TransactionRepository
import dev.jacaceresf.kryptomanager.repositories.WalletMovementRepository
import dev.jacaceresf.kryptomanager.repositories.WalletRepository
import dev.jacaceresf.kryptomanager.utils.WalletUtils
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Service
class WalletServiceImpl(
    private val walletRepository: WalletRepository,
    private val walletMovementRepository: WalletMovementRepository,
    private val transactionRepository: TransactionRepository
) : WalletService {

    override suspend fun getWallets(): Flux<Wallet> = walletRepository.findAll();

    override suspend fun getWalletByAddress(address: String): Wallet {
        return WalletUtils.getWalletFromOptional(walletRepository.findByAddress(address).awaitSingle())
    }

    override suspend fun createWallet(userEmail: String): Wallet {

        val walletByEmail = walletRepository.findByUserEmail(userEmail).awaitSingleOrNull()

        ///set initial balance to zero
        when {
            walletByEmail != null -> {
                throw RuntimeException("Email is already in use.")
            }
            else -> {
                val wallet = Wallet(
                    userEmail = userEmail,
                    address = WalletUtils.createWalletAddress(userEmail),
                    creationDate = LocalDateTime.now(),
                    balance = BigDecimal.ZERO ///set initial balance to zero
                )

                print("Let's save wallet -> $wallet")

                return walletRepository.save(wallet).awaitSingle()
            }
        }

    }


    override suspend fun addFiatBalance(walletFiatReq: WalletFiatReq): Wallet {

        val wallet =
            WalletUtils.getWalletFromOptional(walletRepository.findByAddress(walletFiatReq.walletAddress).awaitSingle())

        if (walletFiatReq.balance < BigDecimal.ZERO) {
            throw RuntimeException("Invalid balance value")
        }
        wallet.balance += walletFiatReq.balance

        walletRepository.save(wallet).awaitSingle()

        val walletMovement = WalletMovement(
            movementId = UUID.randomUUID().toString().uppercase(),
            balance = wallet.balance,
            walletId = wallet.id!!,
            movementType = MovementType.CREDIT,
            timestamp = LocalDateTime.now(),
            description = "USD balance added"
        )

        walletMovementRepository.save(walletMovement).awaitSingle()

        return wallet
    }

    override suspend fun getWalletMovements(address: String, from: String?, to: String?): WalletMovementDetail {

        val wallet = WalletUtils.getWalletFromOptional(walletRepository.findByAddress(address).awaitSingle())

        val movementsList = walletMovementRepository.findByWalletId(wallet.id!!).collectList().awaitSingle()

        return WalletMovementDetail(
            wallet = wallet,
            movements = movementsList
        )
    }

    override suspend fun updateWalletBalanceFromCryptoTransaction(cryptoTransactionResponse: CryptoTransactionResponse) {

        val wallet = WalletUtils.getWalletFromOptional(
            walletRepository.findById(cryptoTransactionResponse.wallet.id!!).awaitSingle()
        )

        val balance = cryptoTransactionResponse.executionPrice * cryptoTransactionResponse.amount

        if (cryptoTransactionResponse.type == CryptoTransactionType.BUY) {
            wallet.balance -= balance
        } else {
            wallet.balance += balance
        }

        walletRepository.save(wallet).awaitSingle()

        saveWalletMovementFromTransaction(cryptoTransactionResponse)
    }

    override suspend fun saveWalletMovementFromTransaction(cryptoTransactionResponse: CryptoTransactionResponse) {

        val movementType = when (cryptoTransactionResponse.type) {
            CryptoTransactionType.BUY -> {
                MovementType.DEBIT
            }
            else -> {
                MovementType.CREDIT
            }
        }

        val walletMovement = WalletMovement(
            movementId = UUID.randomUUID().toString().uppercase(),
            balance = cryptoTransactionResponse.executionPrice * cryptoTransactionResponse.amount,
            walletId = cryptoTransactionResponse.wallet.id!!,
            movementType = movementType,
            timestamp = LocalDateTime.now(),
            description = cryptoTransactionResponse.crypto.symbol + " " + cryptoTransactionResponse.type
        )

        walletMovementRepository.save(walletMovement).awaitSingle()
    }

    override suspend fun getWalletTransactions(walletId: Long): Collection<Transaction> {
        return transactionRepository.findByWalletIdOrderByTimestampAsc(walletId).collectList().awaitSingle()
    }
}