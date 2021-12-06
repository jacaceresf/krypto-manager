package dev.jacaceresf.kryptomanager.utils

import dev.jacaceresf.kryptomanager.exceptions.NotFoundException
import dev.jacaceresf.kryptomanager.models.Crypto

object CryptoUtils {

    fun getCryptoFromOptional(crypto: Crypto?): Crypto {
        return crypto ?: throw NotFoundException("Crypto not found!")
    }

}