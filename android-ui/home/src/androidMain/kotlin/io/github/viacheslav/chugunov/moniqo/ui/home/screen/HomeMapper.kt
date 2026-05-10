package io.github.viacheslav.chugunov.moniqo.ui.home.screen

import io.github.viacheslav.chugunov.moniqo.android.ui.core.R
import io.github.viacheslav.chugunov.moniqo.core.model.Rate
import io.github.viacheslav.chugunov.moniqo.core.model.RatePair
import io.github.viacheslav.chugunov.moniqo.ui.core.StringProvider
import io.github.viacheslav.chugunov.moniqo.ui.core.extensions.asPrice
import io.github.viacheslav.chugunov.moniqo.ui.home.model.CurrencyInfo
import io.github.viacheslav.chugunov.moniqo.ui.home.model.DealQuality
import io.github.viacheslav.chugunov.moniqo.ui.home.model.ExchangeAnalysis
import java.util.Locale
import kotlin.math.abs

internal interface HomeMapper {
    fun toHomeContent(
        pair: RatePair,
        fromAmount: String = "",
    ): HomeState.Content

    fun toExchangeAnalysis(
        fromAmount: String,
        toAmount: String,
        officialRate: Double?,
        fromCurrency: CurrencyInfo,
        toCurrency: CurrencyInfo,
    ): ExchangeAnalysis?

    fun toOfficialRate(pair: RatePair): Double

    fun toCurrencyInfo(rate: Rate): CurrencyInfo
}

internal class HomeMapperImpl(
    private val stringProvider: StringProvider,
) : HomeMapper {
    override fun toHomeContent(
        pair: RatePair,
        fromAmount: String,
    ): HomeState.Content {
        val officialRate = toOfficialRate(pair)
        val fromCurrency = toCurrencyInfo(pair.fromRate)
        val toCurrency = toCurrencyInfo(pair.toRate)
        val toAmount =
            fromAmount.toDoubleOrNull()?.takeIf { it > 0 }?.let {
                String.format(Locale.US, "%.2f", it * officialRate)
            } ?: ""
        return HomeState.Content(
            fromCurrency = fromCurrency,
            toCurrency = toCurrency,
            fromAmount = fromAmount,
            toAmount = toAmount,
            fromHint = stringProvider.get(R.string.home_from_hint, fromCurrency.code, officialRate.asPrice, toCurrency.code),
            toHint =
                fromAmount.toDoubleOrNull()?.takeIf { it > 0 }?.let {
                    stringProvider.get(R.string.home_to_hint, (it * officialRate).asPrice, toCurrency.code)
                } ?: "",
            analysis = toExchangeAnalysis(fromAmount, toAmount, officialRate, fromCurrency, toCurrency),
        )
    }

    override fun toExchangeAnalysis(
        fromAmount: String,
        toAmount: String,
        officialRate: Double?,
        fromCurrency: CurrencyInfo,
        toCurrency: CurrencyInfo,
    ): ExchangeAnalysis? {
        if (officialRate == null) return null
        val from = fromAmount.toDoubleOrNull()?.takeIf { it > 0 } ?: return null
        val to = toAmount.toDoubleOrNull()?.takeIf { it > 0 } ?: return null
        val enteredRate = to / from
        val diffPercent = (officialRate - enteredRate) / enteredRate * 100.0
        val quality =
            when {
                diffPercent < 5.0 -> DealQuality.GOOD
                diffPercent < 10.0 -> DealQuality.MEDIUM
                else -> DealQuality.BAD
            }
        val lossInTo = (officialRate - enteredRate) * from
        val lossInFrom = if (officialRate > 0) lossInTo / officialRate else 0.0
        val isProfit = lossInTo < 0
        return ExchangeAnalysis(
            officialRate = "1 ${fromCurrency.code} = ${officialRate.asPrice} ${toCurrency.code}",
            enteredRate = "1 ${fromCurrency.code} = ${enteredRate.asPrice} ${toCurrency.code}",
            differencePercent = "${diffPercent.asPrice}%",
            lossOrProfitLabel = stringProvider.get(if (isProfit) R.string.exchange_analysis_profit else R.string.exchange_analysis_loss),
            lossOrProfitAmount = "${abs(lossInFrom).asPrice} ${fromCurrency.code}\n${abs(lossInTo).asPrice} ${toCurrency.code}",
            quality = quality,
        )
    }

    override fun toOfficialRate(pair: RatePair): Double = pair.toRate.rate / pair.fromRate.rate

    override fun toCurrencyInfo(rate: Rate): CurrencyInfo {
        val code = rate.currency.uppercase()
        return CurrencyInfo(
            code = code,
            name = CURRENCY_NAME_RES[code]?.let { stringProvider.get(it) } ?: code,
            flag = CURRENCY_FLAGS[code] ?: "🏳️",
        )
    }

    private companion object {
        val CURRENCY_NAME_RES =
            mapOf(
                "EUR" to R.string.currency_name_eur,
                "USD" to R.string.currency_name_usd,
                "GBP" to R.string.currency_name_gbp,
                "JPY" to R.string.currency_name_jpy,
                "CHF" to R.string.currency_name_chf,
                "AUD" to R.string.currency_name_aud,
                "CAD" to R.string.currency_name_cad,
                "CNY" to R.string.currency_name_cny,
                "SEK" to R.string.currency_name_sek,
                "NOK" to R.string.currency_name_nok,
                "DKK" to R.string.currency_name_dkk,
                "NZD" to R.string.currency_name_nzd,
                "SGD" to R.string.currency_name_sgd,
                "HKD" to R.string.currency_name_hkd,
                "KRW" to R.string.currency_name_krw,
                "MXN" to R.string.currency_name_mxn,
                "INR" to R.string.currency_name_inr,
                "BRL" to R.string.currency_name_brl,
                "PLN" to R.string.currency_name_pln,
                "TRY" to R.string.currency_name_try,
                "ZAR" to R.string.currency_name_zar,
                "THB" to R.string.currency_name_thb,
                "MYR" to R.string.currency_name_myr,
                "IDR" to R.string.currency_name_idr,
                "HUF" to R.string.currency_name_huf,
                "CZK" to R.string.currency_name_czk,
                "ILS" to R.string.currency_name_ils,
                "PHP" to R.string.currency_name_php,
                "AED" to R.string.currency_name_aed,
            )

        val CURRENCY_FLAGS =
            mapOf(
                "EUR" to "🇪🇺",
                "USD" to "🇺🇸",
                "GBP" to "🇬🇧",
                "JPY" to "🇯🇵",
                "CHF" to "🇨🇭",
                "AUD" to "🇦🇺",
                "CAD" to "🇨🇦",
                "CNY" to "🇨🇳",
                "SEK" to "🇸🇪",
                "NOK" to "🇳🇴",
                "DKK" to "🇩🇰",
                "NZD" to "🇳🇿",
                "SGD" to "🇸🇬",
                "HKD" to "🇭🇰",
                "KRW" to "🇰🇷",
                "MXN" to "🇲🇽",
                "INR" to "🇮🇳",
                "BRL" to "🇧🇷",
                "PLN" to "🇵🇱",
                "TRY" to "🇹🇷",
                "ZAR" to "🇿🇦",
                "THB" to "🇹🇭",
                "MYR" to "🇲🇾",
                "IDR" to "🇮🇩",
                "HUF" to "🇭🇺",
                "CZK" to "🇨🇿",
                "ILS" to "🇮🇱",
                "PHP" to "🇵🇭",
                "AED" to "🇦🇪",
            )
    }
}
