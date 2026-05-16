package io.github.viacheslav.chugunov.moniqo.ui.home.screen

import io.github.viacheslav.chugunov.moniqo.android.ui.core.R
import io.github.viacheslav.chugunov.moniqo.core.model.Rate
import io.github.viacheslav.chugunov.moniqo.core.model.RatePair
import io.github.viacheslav.chugunov.moniqo.ui.core.StringProvider
import io.github.viacheslav.chugunov.moniqo.ui.core.extensions.asPrice
import io.github.viacheslav.chugunov.moniqo.ui.core.model.CurrencyInfo
import io.github.viacheslav.chugunov.moniqo.ui.core.model.CurrencyMeta
import io.github.viacheslav.chugunov.moniqo.ui.core.model.DealRanges
import io.github.viacheslav.chugunov.moniqo.ui.home.model.DealQuality
import io.github.viacheslav.chugunov.moniqo.ui.home.model.ExchangeAnalysis
import java.util.Locale
import kotlin.math.abs

internal interface HomeMapper {
    fun toHomeContent(
        pair: RatePair,
        fromAmount: String = "",
        dealRanges: DealRanges = DealRanges(),
    ): HomeState.Content

    fun toExchangeAnalysis(
        fromAmount: String,
        toAmount: String,
        officialRate: Double?,
        fromCurrency: CurrencyInfo,
        toCurrency: CurrencyInfo,
        dealRanges: DealRanges = DealRanges(),
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
        dealRanges: DealRanges,
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
            analysis = toExchangeAnalysis(fromAmount, toAmount, officialRate, fromCurrency, toCurrency, dealRanges),
        )
    }

    override fun toExchangeAnalysis(
        fromAmount: String,
        toAmount: String,
        officialRate: Double?,
        fromCurrency: CurrencyInfo,
        toCurrency: CurrencyInfo,
        dealRanges: DealRanges,
    ): ExchangeAnalysis? {
        if (officialRate == null) return null
        val from = fromAmount.toDoubleOrNull()?.takeIf { it > 0 } ?: return null
        val to = toAmount.toDoubleOrNull()?.takeIf { it > 0 } ?: return null
        val enteredRate = to / from
        val diffPercent = (officialRate - enteredRate) / enteredRate * 100.0
        val quality =
            when {
                diffPercent < dealRanges.goodMax -> DealQuality.GOOD
                diffPercent < dealRanges.averageMax -> DealQuality.MEDIUM
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
        val code = rate.currency.name.uppercase()
        return CurrencyInfo(
            code = code,
            name = CurrencyMeta.nameRes[code]?.let { stringProvider.get(it) } ?: code,
            flag = CurrencyMeta.flags[code] ?: "🏳️",
            isCrypto = rate.currency.isCrypto,
        )
    }
}
