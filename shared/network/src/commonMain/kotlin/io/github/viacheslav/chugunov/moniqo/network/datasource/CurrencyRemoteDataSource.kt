package io.github.viacheslav.chugunov.moniqo.network.datasource

import io.github.viacheslav.chugunov.moniqo.network.dto.CurrencyRatesDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

internal interface CurrencyRemoteDataSource {
    suspend fun getEurRates(): CurrencyRatesDto
}

internal class CurrencyRemoteDataSourceImpl(
    private val client: HttpClient,
) : CurrencyRemoteDataSource {
    override suspend fun getEurRates(): CurrencyRatesDto =
        client.get("${BASE_URL}npm/@fawazahmed0/currency-api@latest/v1/currencies/eur.min.json").body()

    companion object {
        private const val BASE_URL = "https://cdn.jsdelivr.net/"
    }
}
