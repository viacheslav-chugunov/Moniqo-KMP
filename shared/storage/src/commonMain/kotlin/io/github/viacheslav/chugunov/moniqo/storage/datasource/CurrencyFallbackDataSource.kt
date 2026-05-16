package io.github.viacheslav.chugunov.moniqo.storage.datasource

import io.github.viacheslav.chugunov.moniqo.core.di.CoroutineDispatchers
import io.github.viacheslav.chugunov.moniqo.core.model.Currency
import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyRates
import io.github.viacheslav.chugunov.moniqo.core.model.Rate
import io.github.viacheslav.chugunov.moniqo.storage.model.FallbackRatesEntry
import io.github.viacheslav.chugunov.moniqo.storage.util.FALLBACK_RATES_JSON
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

interface CurrencyFallbackDataSource {
    suspend fun get(): CurrencyRates
}

internal class CurrencyFallbackDataSourceImpl(
    private val json: Json,
    private val dispatchers: CoroutineDispatchers,
) : CurrencyFallbackDataSource {
    override suspend fun get(): CurrencyRates =
        withContext(dispatchers.default) {
            val entry = json.decodeFromString<FallbackRatesEntry>(FALLBACK_RATES_JSON)
            CurrencyRates(
                updatedAt = entry.date,
                baseCurrency = Currency.of("eur"),
                rates = entry.eur.map { (currency, rate) -> Rate(Currency.of(currency), rate) },
            )
        }
}
