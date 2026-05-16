package io.github.viacheslav.chugunov.moniqo.storage.datasource

import io.github.viacheslav.chugunov.moniqo.core.di.CoroutineDispatchers
import io.github.viacheslav.chugunov.moniqo.core.model.Currency
import io.github.viacheslav.chugunov.moniqo.core.model.Rate
import io.github.viacheslav.chugunov.moniqo.core.model.RatePair
import io.github.viacheslav.chugunov.moniqo.storage.model.FallbackRatesEntry
import io.github.viacheslav.chugunov.moniqo.storage.util.FALLBACK_RATES_JSON
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

internal interface RatePairFallbackDataSource {
    suspend fun get(): RatePair
}

internal class RatePairFallbackDataSourceImpl(
    private val json: Json,
    private val dispatchers: CoroutineDispatchers,
) : RatePairFallbackDataSource {
    override suspend fun get(): RatePair =
        withContext(dispatchers.default) {
            val dto = json.decodeFromString<FallbackRatesEntry>(FALLBACK_RATES_JSON)
            RatePair(
                fromRate = Rate(currency = Currency.of("usd"), rate = dto.eur.getValue("usd")),
                toRate = Rate(currency = Currency.of("eur"), rate = dto.eur.getValue("eur")),
                baseCurrency = Currency.of("eur"),
                updatedAt = dto.date,
            )
        }
}
