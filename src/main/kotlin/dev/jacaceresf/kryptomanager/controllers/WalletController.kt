package dev.jacaceresf.kryptomanager.controllers

import dev.jacaceresf.kryptomanager.models.req.WalletFiatReq
import dev.jacaceresf.kryptomanager.services.WalletBalanceService
import dev.jacaceresf.kryptomanager.services.WalletService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/wallets")
class WalletController(
    private val walletService: WalletService,
    private val walletBalanceService: WalletBalanceService
) {

    @GetMapping
    suspend fun getAllWallets() = walletService.getWallets()

    @GetMapping("/address/{address}")
    suspend fun getWalletByAddress(@PathVariable("address") address: String) = walletService.getWalletByAddress(address)

    @PostMapping("/create/{email}")
    suspend fun saveWallet(@PathVariable("email") email: String) = walletService.createWallet(email)

    @PatchMapping("/balance")
    suspend fun addFiatBalance(@RequestBody walletFiatReq: WalletFiatReq) =
        walletService.addFiatBalance(walletFiatReq)

    @GetMapping("/{address}/movements")
    suspend fun getWalletMovements(
        @PathVariable("address") address: String,
        @RequestParam(value = "from", required = false) fromDate: String?,
        @RequestParam(value = "to", required = false) toDate: String?
    ) = walletService.getWalletMovements(address, fromDate, toDate)

    @GetMapping("/{address}/balance/crypto")
    suspend fun getWalletCompositeBalance(@PathVariable("address") address: String) =
        walletBalanceService.getWalletCompositeBalance(address)
}