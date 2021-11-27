package dev.jacaceresf.kryptomanager.utils

import dev.jacaceresf.kryptomanager.exceptions.NotFoundException
import dev.jacaceresf.kryptomanager.models.Crypto
import java.util.*

object CryptoUtils {

    fun getCryptoFromOptional(crypto: Optional<Crypto>): Crypto {
        return if (crypto.isPresent) crypto.get() else throw NotFoundException("Crypto not found!")
    }

}