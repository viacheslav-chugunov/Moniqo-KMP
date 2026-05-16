package io.github.viacheslav.chugunov.moniqo.ui.rates.screen

import io.github.viacheslav.chugunov.moniqo.android.ui.core.R
import io.github.viacheslav.chugunov.moniqo.core.model.Currency
import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyRates
import io.github.viacheslav.chugunov.moniqo.ui.core.StringProvider
import io.github.viacheslav.chugunov.moniqo.ui.core.model.CurrencyInfo
import io.github.viacheslav.chugunov.moniqo.ui.core.model.CurrencyMeta
import io.github.viacheslav.chugunov.moniqo.ui.rates.model.RateItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

internal interface RatesMapper {
    fun toContent(rates: CurrencyRates): RatesState.Content
}

internal class RatesMapperImpl(
    private val stringProvider: StringProvider,
) : RatesMapper {
    override fun toContent(rates: CurrencyRates): RatesState.Content =
        RatesState.Content(
            baseCurrency = toCurrencyInfo(rates.baseCurrency),
            updatedAt = stringProvider.get(R.string.rates_updated, formatDate(rates.updatedAt)),
            rates =
                rates.rates.map { rate ->
                    RateItem(
                        currency = toCurrencyInfo(rate.currency),
                        rate = rate.rate.asRate,
                    )
                },
        )

    private fun toCurrencyInfo(currency: Currency): CurrencyInfo {
        val code = currency.name.uppercase()
        return CurrencyInfo(
            code = code,
            name = CurrencyMeta.nameRes[code]?.let { stringProvider.get(it) } ?: code,
            flag = CurrencyMeta.flags[code] ?: "🏳️",
            isCrypto = currency.isCrypto,
        )
    }

    private fun formatDate(dateStr: String): String =
        try {
            val date = LocalDate.parse(dateStr)
            if (date == LocalDate.now()) {
                stringProvider.get(R.string.rates_date_today)
            } else {
                date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
            }
        } catch (e: Exception) {
            dateStr
        }
}

private val Double.asRate: String
    get() =
        if (this >= 100) String.format(Locale.US, "%.2f", this)
        else String.format(Locale.US, "%.4f", this)
