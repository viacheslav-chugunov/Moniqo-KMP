package io.github.viacheslav.chugunov.moniqo.ui.choosecurrency.screen

import io.github.viacheslav.chugunov.moniqo.core.model.Currency
import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyRates
import io.github.viacheslav.chugunov.moniqo.core.model.RatePair
import io.github.viacheslav.chugunov.moniqo.ui.core.StringProvider
import io.github.viacheslav.chugunov.moniqo.ui.core.model.CurrencyInfo
import io.github.viacheslav.chugunov.moniqo.ui.core.model.CurrencyMeta

internal interface ChooseCurrencyMapper {
    fun toContent(rates: CurrencyRates, pair: RatePair): ChooseCurrencyState.Content
}

internal class ChooseCurrencyMapperImpl(
    private val stringProvider: StringProvider,
) : ChooseCurrencyMapper {
    override fun toContent(rates: CurrencyRates, pair: RatePair): ChooseCurrencyState.Content {
        val allCurrencies = buildList {
            add(toCurrencyInfo(rates.baseCurrency))
            rates.rates.forEach { add(toCurrencyInfo(it.currency)) }
        }.sortedBy { it.code }

        val fromInfo = toCurrencyInfo(pair.fromRate.currency)
        val toInfo = toCurrencyInfo(pair.toRate.currency)
        val recent = listOf(fromInfo, toInfo).distinctBy { it.code }

        return ChooseCurrencyState.Content(
            recentCurrencies = recent,
            currencies = allCurrencies,
        )
    }

    private fun toCurrencyInfo(currency: Currency): CurrencyInfo {
        val code = currency.name.uppercase()
        return CurrencyInfo(
            code = code,
            name = CurrencyMeta.nameRes[code]?.let { stringProvider.get(it) } ?: code,
            flag = CurrencyMeta.flags[code] ?: "🏳️",
            isCrypto = currency.isCrypto,
        )
    }
}
