package dev.jacaceresf.kryptomanager.models

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id


@Entity
data class Wallet(
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "wallet_id_seq")
    val id: Long = -1,
    val userEmail: String,
    val address: String,
    val creationDate: LocalDateTime
) {
    constructor() : this(userEmail = "", address = "", creationDate = LocalDateTime.now()) {

    }

}