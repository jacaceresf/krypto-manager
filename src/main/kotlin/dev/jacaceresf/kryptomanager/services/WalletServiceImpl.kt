package dev.jacaceresf.kryptomanager.services

import dev.jacaceresf.kryptomanager.exceptions.NotFoundException
import dev.jacaceresf.kryptomanager.models.Wallet
import dev.jacaceresf.kryptomanager.repositories.WalletRepository
import dev.jacaceresf.kryptomanager.utils.WalletUtils
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class WalletServiceImpl(private val walletRepository: WalletRepository) : WalletService {

    override fun getWallets(): MutableIterable<Wallet> = walletRepository.findAll();

    override fun getWalletByAddress(address: String): Wallet {
        val wallet = walletRepository.findByAddress(address)
        if (!wallet.isPresent) {
            throw NotFoundException("Wallet with address $address not found!")
        }

        return wallet.get()
    }

    override fun createWallet(userEmail: String) : Wallet {

        val wallet = Wallet(
            id = -1,
            userEmail = userEmail,
            address = WalletUtils.createWalletAddress(userEmail),
            creationDate = LocalDateTime.now()
        )

        print("Let's save wallet -> $wallet")

        return walletRepository.save(wallet)
    }

    override fun addFiatBalance(address: String, fiatBalance: BigDecimal) {
        TODO("Not yet implemented")
    }

}