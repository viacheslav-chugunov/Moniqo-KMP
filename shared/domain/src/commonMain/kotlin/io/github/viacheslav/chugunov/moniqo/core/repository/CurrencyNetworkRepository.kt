package io.github.viacheslav.chugunov.moniqo.core.repository

import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyRates

interface CurrencyNetworkRepository {
    suspend fun getRates(): CurrencyRates
}
