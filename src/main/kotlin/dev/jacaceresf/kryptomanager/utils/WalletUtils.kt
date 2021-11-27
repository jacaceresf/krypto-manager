package dev.jacaceresf.kryptomanager.utils

import dev.jacaceresf.kryptomanager.exceptions.NotFoundException
import dev.jacaceresf.kryptomanager.models.Wallet
import java.security.MessageDigest
import java.time.LocalDateTime
import java.util.*

object WalletUtils {

    private val MD5_ENCODER: MessageDigest = MessageDigest.getInstance("MD5")

    fun createWalletAddress(ownerEmail: String): String {

        val now = LocalDateTime.now().toString()

        val wallet = ownerEmail + now

        return MD5_ENCODER.digest(wallet.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }

    fun getWalletFromOptional(wallet: Optional<Wallet>): Wallet {
        return if (wallet.isPresent) wallet.get() else throw NotFoundException("Wallet not found!")
    }

}