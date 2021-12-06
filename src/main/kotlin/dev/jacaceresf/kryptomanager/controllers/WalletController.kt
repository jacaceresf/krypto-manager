package dev.jacaceresf.kryptomanager.controllers

import dev.jacaceresf.kryptomanager.models.Wallet
import dev.jacaceresf.kryptomanager.models.WalletMovementDetail
import dev.jacaceresf.kryptomanager.models.req.WalletFiatReq
import dev.jacaceresf.kryptomanager.services.WalletBalanceService
import dev.jacaceresf.kryptomanager.services.WalletService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/wallets")
class WalletController(
    private val walletService: WalletService,
    private val walletBalanceService: WalletBalanceService
) {

    @GetMapping
    suspend fun getAllWallets(): Flux<Wallet> = walletService.getWallets()

    @GetMapping("/address/{address}")
    suspend fun getWalletByAddress(@PathVariable("address") address: String): Wallet = walletService.getWalletByAddress(address)

    @PostMapping("/create/{email}")
    suspend fun saveWallet(@PathVariable("email") email: String): Wallet = walletService.createWallet(email)

    @PatchMapping("/balance")
    suspend fun addFiatBalance(@RequestBody walletFiatReq: WalletFiatReq): Wallet =
        walletService.addFiatBalance(walletFiatReq)

    @GetMapping("/{address}/movements")
    suspend fun getWalletMovements(
        @PathVariable("address") address: String,
        @RequestParam(value = "from", required = false) fromDate: String?,
        @RequestParam(value = "to", required = false) toDate: String?
    ): WalletMovementDetail = walletService.getWalletMovements(address, fromDate, toDate)

    @GetMapping("/{address}/balance/crypto")
    suspend fun getWalletCompositeBalance(@PathVariable("address") address: String) =
        walletBalanceService.getWalletCompositeBalance(address)
}