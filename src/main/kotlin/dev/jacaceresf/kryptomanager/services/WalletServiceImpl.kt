package dev.jacaceresf.kryptomanager.services

import dev.jacaceresf.kryptomanager.models.MovementType
import dev.jacaceresf.kryptomanager.models.Wallet
import dev.jacaceresf.kryptomanager.models.WalletMovement
import dev.jacaceresf.kryptomanager.models.WalletMovementDetail
import dev.jacaceresf.kryptomanager.models.req.CryptoTransactionResponse
import dev.jacaceresf.kryptomanager.models.req.CryptoTransactionType
import dev.jacaceresf.kryptomanager.models.req.WalletFiatReq
import dev.jacaceresf.kryptomanager.repositories.WalletMovementRepository
import dev.jacaceresf.kryptomanager.repositories.WalletRepository
import dev.jacaceresf.kryptomanager.utils.WalletUtils
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Service
class WalletServiceImpl(
    private val walletRepository: WalletRepository,
    private val walletMovementRepository: WalletMovementRepository
) : WalletService {

    override fun getWallets(): MutableIterable<Wallet> = walletRepository.findAll();

    override fun getWalletByAddress(address: String): Wallet {
        return WalletUtils.getWalletFromOptional(walletRepository.findByAddress(address))
    }

    override fun createWallet(userEmail: String): Wallet {

        val wallet = Wallet(
            id = -1,
            userEmail = userEmail,
            address = WalletUtils.createWalletAddress(userEmail),
            creationDate = LocalDateTime.now(),
            balance = BigDecimal.ZERO ///set initial balance to zero
        )

        print("Let's save wallet -> $wallet")

        return walletRepository.save(wallet)
    }


    override fun addFiatBalance(walletFiatReq: WalletFiatReq): Wallet {

        val wallet = WalletUtils.getWalletFromOptional(walletRepository.findByAddress(walletFiatReq.walletAddress))

        if (walletFiatReq.balance < BigDecimal.ZERO) {
            throw RuntimeException("Invalid balance value")
        }
        wallet.balance += walletFiatReq.balance

        walletRepository.save(wallet)

        val walletMovement = WalletMovement(
            movementId = UUID.randomUUID().toString().uppercase(),
            balance = wallet.balance,
            walletId = wallet.id,
            movementType = MovementType.CREDIT,
            timestamp = LocalDateTime.now(),
            description = "USD balance added"
        )

        walletMovementRepository.save(walletMovement)

        return wallet
    }

    override fun getWalletMovements(address: String, from: String?, to: String?): WalletMovementDetail {

        val wallet = WalletUtils.getWalletFromOptional(walletRepository.findByAddress(address))

        val movements = walletMovementRepository.findByWalletId(wallet.id)

        return WalletMovementDetail(
            wallet = wallet,
            movements = movements
        )
    }

    override fun updateWalletBalanceFromCryptoTransaction(cryptoTransactionResponse: CryptoTransactionResponse) {

        val wallet = WalletUtils.getWalletFromOptional(walletRepository.findById(cryptoTransactionResponse.wallet.id))

        val balance = cryptoTransactionResponse.executionPrice * cryptoTransactionResponse.amount

        if (cryptoTransactionResponse.type == CryptoTransactionType.BUY) {
            wallet.balance -= balance
        } else {
            wallet.balance += balance
        }

        walletRepository.save(wallet)

        saveWalletMovementFromTransaction(cryptoTransactionResponse)
    }

    override fun saveWalletMovementFromTransaction(cryptoTransactionResponse: CryptoTransactionResponse) {

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
            walletId = cryptoTransactionResponse.wallet.id,
            movementType = movementType,
            timestamp = LocalDateTime.now(),
            description = cryptoTransactionResponse.crypto.symbol + " " + cryptoTransactionResponse.type
        )

        walletMovementRepository.save(walletMovement)
    }
}