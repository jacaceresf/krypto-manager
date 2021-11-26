package dev.jacaceresf.kryptomanager.repositories

import dev.jacaceresf.kryptomanager.models.WalletMovement
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface WalletMovementRepository : CrudRepository<WalletMovement, Long> {

    fun findByWalletAddress(walletAddress: String): Collection<WalletMovement>

}