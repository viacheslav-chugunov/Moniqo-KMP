package io.github.viacheslav.chugunov.moniqo.network.datasource

import io.github.viacheslav.chugunov.moniqo.core.di.CoroutineDispatchers
import io.github.viacheslav.chugunov.moniqo.network.dto.CurrencyRatesDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.withContext

interface CurrencyRemoteDataSource {
    suspend fun getEurRates(): CurrencyRatesDto
}

internal class CurrencyRemoteDataSourceImpl(
    private val client: HttpClient,
    private val dispatchers: CoroutineDispatchers,
) : CurrencyRemoteDataSource {
    override suspend fun getEurRates(): CurrencyRatesDto =
        withContext(dispatchers.io) {
            client
                .get("${BASE_URL}npm/@fawazahmed0/currency-api@latest/v1/currencies/eur.min.json")
                .body()
        }

    companion object {
        private const val BASE_URL = "https://cdn.jsdelivr.net/"
    }
}
