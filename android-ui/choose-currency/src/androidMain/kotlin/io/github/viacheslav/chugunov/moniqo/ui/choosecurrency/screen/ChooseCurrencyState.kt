package io.github.viacheslav.chugunov.moniqo.ui.choosecurrency.screen

import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyFilter
import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyInfo

internal sealed interface ChooseCurrencyState {
    data object Loading : ChooseCurrencyState

    data class Content(
        val recentCurrencies: List<CurrencyInfo>,
        val currencies: List<CurrencyInfo>,
        val query: String = "",
        val filter: CurrencyFilter = CurrencyFilter.All,
    ) : ChooseCurrencyState {
        val filteredCurrencies: List<CurrencyInfo>
            get() =
                currencies
                    .let { list ->
                        when (filter) {
                            CurrencyFilter.All -> list
                            CurrencyFilter.Fiat -> list.filter { !it.isCrypto }
                            CurrencyFilter.Crypto -> list.filter { it.isCrypto }
                        }
                    }
                    .let { list ->
                        if (query.isBlank()) {
                            list
                        } else {
                            list.filter { currency ->
                                currency.code.contains(query, ignoreCase = true) ||
                                    currency.name.contains(query, ignoreCase = true)
                            }
                        }
                    }

        val displayedRecent: List<CurrencyInfo>
            get() =
                if (query.isNotBlank()) {
                    emptyList()
                } else {
                    recentCurrencies.filter { recent ->
                        when (filter) {
                            CurrencyFilter.All -> true
                            CurrencyFilter.Fiat -> !recent.isCrypto
                            CurrencyFilter.Crypto -> recent.isCrypto
                        }
                    }
                }

        val groupedCurrencies: Map<Char, List<CurrencyInfo>>
            get() =
                filteredCurrencies
                    .distinctBy { it.code }
                    .groupBy { it.code.first() }
                    .toSortedMap()

        companion object {
            val PREVIEW =
                Content(
                    recentCurrencies =
                        listOf(
                            CurrencyInfo("EUR", "Euro", "🇪🇺", isCrypto = false),
                            CurrencyInfo("USD", "US Dollar", "🇺🇸", isCrypto = false),
                            CurrencyInfo("USDT", "Tether", "🪙", isCrypto = true),
                        ),
                    currencies =
                        listOf(
                            CurrencyInfo("AED", "UAE Dirham", "🇦🇪", isCrypto = false),
                            CurrencyInfo("AUD", "Australian Dollar", "🇦🇺", isCrypto = false),
                            CurrencyInfo("ARS", "Argentine Peso", "🇦🇷", isCrypto = false),
                            CurrencyInfo("BTC", "Bitcoin", "🪙", isCrypto = true),
                            CurrencyInfo("BCH", "Bitcoin Cash", "🪙", isCrypto = true),
                            CurrencyInfo("CAD", "Canadian Dollar", "🇨🇦", isCrypto = false),
                            CurrencyInfo("CHF", "Swiss Franc", "🇨🇭", isCrypto = false),
                            CurrencyInfo("CNY", "Chinese Yuan", "🇨🇳", isCrypto = false),
                        ),
                )
        }
    }
}
