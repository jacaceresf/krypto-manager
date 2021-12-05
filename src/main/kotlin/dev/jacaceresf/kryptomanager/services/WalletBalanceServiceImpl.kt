package dev.jacaceresf.kryptomanager.services

import dev.jacaceresf.kryptomanager.models.CryptoBalance
import dev.jacaceresf.kryptomanager.models.Transaction
import dev.jacaceresf.kryptomanager.models.TransactionType
import dev.jacaceresf.kryptomanager.models.WalletCompositeBalance
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class WalletBalanceServiceImpl(
    private val walletService: WalletService,
    private val cryptoService: CryptoService
) :
    WalletBalanceService {

    override suspend fun getWalletCompositeBalance(walletAddress: String): WalletCompositeBalance {
        ///get wallet by address
        val wallet = walletService.getWalletByAddress(walletAddress)

        ///get wallet transactions
        val walletTransactions = walletService.getWalletTransactions(walletId = wallet.id)

        val cryptoBalanceCollection = mutableListOf<CryptoBalance>()
        ///iterate over transactions and determine amount

        val groupedTransactions: Map<Long, List<Transaction>> =
            walletTransactions.sortedBy { it.timestamp }.groupBy { it.cryptoId }

        var currentPortfolioValue = BigDecimal.ZERO

        for (transaction in groupedTransactions) {

            var balance = BigDecimal.ZERO
            for (tx in transaction.value) {
                balance += if (tx.type == TransactionType.BUY) {
                    tx.amount
                } else {
                    tx.amount.negate()
                }
            }

            val cryptoCurrentPrice = cryptoService.getCryptoCurrentPriceFromId(transaction.key)
            val crypto = cryptoService.getCryptoBySymbol(cryptoCurrentPrice.symbol)

            val cryptoBalance = CryptoBalance(
                crypto = crypto,
                balance = balance,
                currentValue = balance * cryptoCurrentPrice.currentPrice
            )

            cryptoBalanceCollection.add(cryptoBalance)

            currentPortfolioValue += cryptoBalance.currentValue
        }

        ///get crypto current price so we can determine current wallet value

        ///return response
        return WalletCompositeBalance(
            wallet = wallet,
            cryptoBalance = cryptoBalanceCollection,
            currentCryptoValues = currentPortfolioValue
        )
    }
}