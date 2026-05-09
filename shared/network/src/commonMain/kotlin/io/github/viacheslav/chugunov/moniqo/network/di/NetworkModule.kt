package io.github.viacheslav.chugunov.moniqo.network.di

import io.github.viacheslav.chugunov.moniqo.core.repository.CurrencyNetworkRepository
import io.github.viacheslav.chugunov.moniqo.network.datasource.CurrencyRemoteDataSource
import io.github.viacheslav.chugunov.moniqo.network.datasource.CurrencyRemoteDataSourceImpl
import io.github.viacheslav.chugunov.moniqo.network.mapper.CurrencyRatesMapper
import io.github.viacheslav.chugunov.moniqo.network.mapper.CurrencyRatesMapperImpl
import io.github.viacheslav.chugunov.moniqo.network.repository.CurrencyNetworkRepositoryImpl
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single<HttpClient> {
        HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            install(Logging) {
                level = LogLevel.INFO
            }
        }
    }

    factory<CurrencyRemoteDataSource> {
        CurrencyRemoteDataSourceImpl(get())
    }

    factory<CurrencyRatesMapper> {
        CurrencyRatesMapperImpl()
    }

    single<CurrencyNetworkRepository> {
        CurrencyNetworkRepositoryImpl(get(), get(), get())
    }
}
