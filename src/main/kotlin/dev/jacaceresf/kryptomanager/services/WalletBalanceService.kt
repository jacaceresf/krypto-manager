package dev.jacaceresf.kryptomanager.services

import dev.jacaceresf.kryptomanager.models.WalletCompositeBalance

interface WalletBalanceService {

    fun getWalletCompositeBalance(walletAddress: String): WalletCompositeBalance

}