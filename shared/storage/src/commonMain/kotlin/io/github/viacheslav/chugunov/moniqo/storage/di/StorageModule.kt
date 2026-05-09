package io.github.viacheslav.chugunov.moniqo.storage.di

import app.cash.sqldelight.db.SqlDriver
import io.github.viacheslav.chugunov.moniqo.core.repository.CurrencyStorageRepository
import io.github.viacheslav.chugunov.moniqo.storage.datasource.CurrencyFallbackDataSource
import io.github.viacheslav.chugunov.moniqo.storage.datasource.CurrencyFallbackDataSourceImpl
import io.github.viacheslav.chugunov.moniqo.storage.datasource.CurrencyLocalDataSource
import io.github.viacheslav.chugunov.moniqo.storage.datasource.CurrencyLocalDataSourceImpl
import io.github.viacheslav.chugunov.moniqo.storage.db.AppDatabase
import io.github.viacheslav.chugunov.moniqo.storage.mapper.CurrencyStorageMapper
import io.github.viacheslav.chugunov.moniqo.storage.mapper.CurrencyStorageMapperImpl
import io.github.viacheslav.chugunov.moniqo.storage.platform.DatabaseDriverFactory
import io.github.viacheslav.chugunov.moniqo.storage.repository.CurrencyStorageRepositoryImpl
import org.koin.dsl.module

val storageModule = module {
    factory<SqlDriver> {
        get<DatabaseDriverFactory>().create()
    }

    single<AppDatabase> {
        AppDatabase(get())
    }

    factory<CurrencyStorageMapper> {
        CurrencyStorageMapperImpl()
    }

    factory<CurrencyLocalDataSource> {
        CurrencyLocalDataSourceImpl(get(), get())
    }

    factory<CurrencyFallbackDataSource> {
        CurrencyFallbackDataSourceImpl()
    }

    single<CurrencyStorageRepository> {
        CurrencyStorageRepositoryImpl(get(), get(), get())
    }
}
