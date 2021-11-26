package dev.jacaceresf.kryptomanager.utils

import dev.jacaceresf.kryptomanager.exceptions.NotFoundException
import dev.jacaceresf.kryptomanager.models.Wallet
import java.security.MessageDigest
import java.time.LocalDateTime
import java.util.*

object WalletUtils {

    fun createWalletAddress(ownerEmail: String): String {

        val now = LocalDateTime.now().toString()

        val wallet = ownerEmail + now

        val digest = MessageDigest.getInstance("MD5").digest(wallet.toByteArray())

        return digest.joinToString("") { "%02x".format(it) }
    }

    fun getWalletFromOptional(wallet: Optional<Wallet>): Wallet {
        return if (wallet.isPresent) wallet.get() else throw NotFoundException("Wallet not found!")
    }

}