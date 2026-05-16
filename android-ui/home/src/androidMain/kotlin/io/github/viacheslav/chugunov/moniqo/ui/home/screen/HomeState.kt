package io.github.viacheslav.chugunov.moniqo.ui.home.screen

import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyInfo
import io.github.viacheslav.chugunov.moniqo.core.model.DealQuality
import io.github.viacheslav.chugunov.moniqo.ui.core.model.ExchangeAnalysis

internal sealed interface HomeState {
    data object Loading : HomeState

    data class Content(
        val fromCurrency: CurrencyInfo,
        val toCurrency: CurrencyInfo,
        val fromAmount: String = "",
        val toAmount: String = "",
        val fromHint: String = "",
        val toHint: String = "",
        val analysis: ExchangeAnalysis? = null,
    ) : HomeState {
        companion object {
            val PREVIEW =
                Content(
                    fromCurrency = CurrencyInfo("EUR", "Euro", "🇪🇺", isCrypto = false),
                    toCurrency = CurrencyInfo("USD", "US Dollar", "🇺🇸", isCrypto = false),
                    fromAmount = "1,000.00",
                    toAmount = "1,200.00",
                    fromHint = "Official: 1 EUR = 1.1700 USD",
                    toHint = "At official rate ≈ 1,170.00 USD",
                    analysis =
                        ExchangeAnalysis(
                            officialRate = "1 EUR = 1.17 USD",
                            enteredRate = "1 EUR = 1.20 USD",
                            differencePercent = "2.56%",
                            lossOrProfitLabel = "Loss",
                            lossOrProfitAmount = "0.03 EUR\n0.04 USD",
                            quality = DealQuality.GOOD,
                        ),
                )
        }
    }
}
