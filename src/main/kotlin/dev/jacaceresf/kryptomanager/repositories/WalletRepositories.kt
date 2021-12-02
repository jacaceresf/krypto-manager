package dev.jacaceresf.kryptomanager.repositories

import dev.jacaceresf.kryptomanager.models.Wallet
import dev.jacaceresf.kryptomanager.models.WalletMovement
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface WalletRepository : CrudRepository<Wallet, Long> {

    fun findByAddress(address: String): Optional<Wallet>

    fun findByUserEmail(userEmail: String): Optional<Wallet>
}

@Repository
interface WalletMovementRepository : CrudRepository<WalletMovement, Long> {

    fun findByWalletId(walletId: Long): Collection<WalletMovement>

}