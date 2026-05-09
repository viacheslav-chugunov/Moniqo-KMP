package io.github.viacheslav.chugunov.moniqo.core.repository

import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyRates

interface CurrencyStorageRepository {
    suspend fun save(rates: CurrencyRates)
    suspend fun getCache(): CurrencyRates
}
