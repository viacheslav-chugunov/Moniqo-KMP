package io.github.viacheslav.chugunov.moniqo.ui.rates.screen

import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyFilter
import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyInfo
import io.github.viacheslav.chugunov.moniqo.ui.core.model.RateItem

internal sealed interface RatesState {
    data object Loading : RatesState

    data class Content(
        val baseCurrency: CurrencyInfo,
        val updatedAt: String,
        val rates: List<RateItem>,
        val isRefreshing: Boolean,
        val query: String,
        val filter: CurrencyFilter,
    ) : RatesState {
        val displayedRates: List<RateItem>
            get() =
                rates
                    .let { list ->
                        when (filter) {
                            CurrencyFilter.All -> list
                            CurrencyFilter.Fiat -> list.filter { !it.currency.isCrypto }
                            CurrencyFilter.Crypto -> list.filter { it.currency.isCrypto }
                        }
                    }
                    .let { list ->
                        if (query.isBlank()) {
                            list
                        } else {
                            list.filter { rate ->
                                rate.currency.code.contains(query, ignoreCase = true) ||
                                    rate.currency.name.contains(query, ignoreCase = true)
                            }
                        }
                    }

        companion object {
            val PREVIEW =
                Content(
                    baseCurrency = CurrencyInfo("EUR", "Euro", "🇪🇺", isCrypto = false),
                    updatedAt = "Updated: Today",
                    rates =
                        listOf(
                            RateItem(CurrencyInfo("USD", "US Dollar", "🇺🇸", isCrypto = false), "1.0934"),
                            RateItem(CurrencyInfo("GBP", "British Pound", "🇬🇧", isCrypto = false), "0.8547"),
                            RateItem(CurrencyInfo("JPY", "Japanese Yen", "🇯🇵", isCrypto = false), "172.38"),
                            RateItem(CurrencyInfo("CHF", "Swiss Franc", "🇨🇭", isCrypto = false), "0.9683"),
                            RateItem(CurrencyInfo("CAD", "Canadian Dollar", "🇨🇦", isCrypto = false), "1.4786"),
                            RateItem(CurrencyInfo("BTC", "BTC", "🏳️", isCrypto = true), "0.000015"),
                            RateItem(CurrencyInfo("ETH", "ETH", "🏳️", isCrypto = true), "0.000514"),
                        ),
                    isRefreshing = false,
                    query = "",
                    filter = CurrencyFilter.All,
                )
        }
    }
}
