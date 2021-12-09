package dev.jacaceresf.kryptomanager.services

import dev.jacaceresf.kryptomanager.exceptions.NotFoundException
import dev.jacaceresf.kryptomanager.models.MovementType
import dev.jacaceresf.kryptomanager.models.Wallet
import dev.jacaceresf.kryptomanager.models.WalletMovement
import dev.jacaceresf.kryptomanager.models.WalletMovementDetail
import dev.jacaceresf.kryptomanager.models.req.CryptoTransactionResponse
import dev.jacaceresf.kryptomanager.models.req.CryptoTransactionType
import dev.jacaceresf.kryptomanager.models.req.WalletFiatReq
import dev.jacaceresf.kryptomanager.repositories.TransactionRepository
import dev.jacaceresf.kryptomanager.repositories.WalletMovementRepository
import dev.jacaceresf.kryptomanager.repositories.WalletRepository
import dev.jacaceresf.kryptomanager.utils.WalletUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Service
class WalletServiceImpl(
    private val walletRepository: WalletRepository,
    private val walletMovementRepository: WalletMovementRepository,
    private val transactionRepository: TransactionRepository
) : WalletService {

    override suspend fun getWallets(): Flow<Wallet> = walletRepository.findAll().asFlow();

    override suspend fun getWalletByAddress(address: String): Mono<Wallet> {
        return walletRepository.findByAddress(address).switchIfEmpty(
            Mono.error(
                NotFoundException("Wallet with address $address not found")
            )
        )
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

        val wallet = getWalletByAddress(walletFiatReq.walletAddress).awaitSingle()

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

        val wallet = getWalletByAddress(address).awaitSingle()

        return WalletMovementDetail(
            wallet = wallet,
            movements = walletMovementRepository.findByWalletId(wallet.id!!).toCollection(mutableListOf())
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

    override suspend fun getWalletTransactions(walletId: Long) =
        transactionRepository.findByWalletIdOrderByTimestampAsc(walletId).toCollection(mutableListOf())

}