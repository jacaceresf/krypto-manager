package dev.jacaceresf.kryptomanager.repositories

import dev.jacaceresf.kryptomanager.models.Transaction
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository : CrudRepository<Transaction, Long>