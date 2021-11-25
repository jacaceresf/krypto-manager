package dev.jacaceresf.kryptomanager.controllers

import dev.jacaceresf.kryptomanager.models.Wallet
import dev.jacaceresf.kryptomanager.services.WalletService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/wallet")
class WalletController(private val walletService: WalletService) {

    @GetMapping
    fun getAllWallets(): MutableIterable<Wallet> = walletService.getWallets()

    @GetMapping("/address/{address}")
    fun getWalletByAddress(@PathVariable("address") address: String): Wallet = walletService.getWalletByAddress(address)

    @PostMapping("/save/{email}")
    fun saveWallet(@PathVariable("email") email: String): Wallet = walletService.createWallet(email)
}