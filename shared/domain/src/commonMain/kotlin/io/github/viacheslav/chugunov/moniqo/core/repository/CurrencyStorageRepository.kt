package io.github.viacheslav.chugunov.moniqo.core.repository

import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyRates

interface CurrencyStorageRepository {
    suspend fun saveRates(rates: CurrencyRates)

    suspend fun getRates(): CurrencyRates

    suspend fun isEmpty(): Boolean
}
