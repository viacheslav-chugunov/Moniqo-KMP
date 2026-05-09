package io.github.viacheslav.chugunov.moniqo.storage.datasource

import io.github.viacheslav.chugunov.moniqo.core.di.CoroutineDispatchers
import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyRates
import io.github.viacheslav.chugunov.moniqo.storage.db.AppDatabase
import io.github.viacheslav.chugunov.moniqo.storage.model.CurrencyRatesRecord
import kotlinx.coroutines.withContext

interface CurrencyLocalDataSource {
    suspend fun save(rates: CurrencyRates)

    suspend fun get(): CurrencyRatesRecord?
}

internal class CurrencyLocalDataSourceImpl(
    database: AppDatabase,
    private val dispatchers: CoroutineDispatchers,
) : CurrencyLocalDataSource {
    private val currencyRatesEntityQueries = database.currencyRatesEntityQueries
    private val rateEntityQueries = database.rateEntityQueries

    override suspend fun save(rates: CurrencyRates) =
        withContext(dispatchers.io) {
            rateEntityQueries.transaction {
                currencyRatesEntityQueries.clearCurrencyRates()
                rateEntityQueries.clearRates()
                currencyRatesEntityQueries.insertCurrencyRates(
                    updated_at = rates.updatedAt,
                    base_currency = rates.baseCurrency,
                )
                rates.rates.forEach { rate ->
                    rateEntityQueries.insertRate(currency = rate.currency, rate = rate.rate)
                }
            }
        }

    override suspend fun get(): CurrencyRatesRecord? =
        withContext(dispatchers.io) {
            val entity = currencyRatesEntityQueries.getCurrencyRates().executeAsOneOrNull() ?: return@withContext null
            val rates = rateEntityQueries.getRates().executeAsList()
            CurrencyRatesRecord(entity, rates)
        }
}
