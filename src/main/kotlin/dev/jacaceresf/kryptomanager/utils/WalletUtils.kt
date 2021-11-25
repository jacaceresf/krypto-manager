package dev.jacaceresf.kryptomanager.utils

import java.security.MessageDigest
import java.time.LocalDateTime

object WalletUtils {

    fun createWalletAddress(ownerEmail: String): String {

        val now = LocalDateTime.now().toString()

        val wallet = ownerEmail + now

        val digest = MessageDigest.getInstance("MD5").digest(wallet.toByteArray())

        return digest.joinToString("") { "%02x".format(it) }
    }

}