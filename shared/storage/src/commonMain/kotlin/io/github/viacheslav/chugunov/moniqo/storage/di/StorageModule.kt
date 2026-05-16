package io.github.viacheslav.chugunov.moniqo.storage.di

import app.cash.sqldelight.db.SqlDriver
import io.github.viacheslav.chugunov.moniqo.core.repository.CurrencyStorageRepository
import io.github.viacheslav.chugunov.moniqo.core.repository.RatePairStorageRepository
import io.github.viacheslav.chugunov.moniqo.core.repository.SettingStorageRepository
import io.github.viacheslav.chugunov.moniqo.storage.datasource.CurrencyFallbackDataSource
import io.github.viacheslav.chugunov.moniqo.storage.datasource.CurrencyFallbackDataSourceImpl
import io.github.viacheslav.chugunov.moniqo.storage.datasource.CurrencyLocalDataSource
import io.github.viacheslav.chugunov.moniqo.storage.datasource.CurrencyLocalDataSourceImpl
import io.github.viacheslav.chugunov.moniqo.storage.datasource.RatePairFallbackDataSource
import io.github.viacheslav.chugunov.moniqo.storage.datasource.RatePairFallbackDataSourceImpl
import io.github.viacheslav.chugunov.moniqo.storage.datasource.RatePairLocalDataSource
import io.github.viacheslav.chugunov.moniqo.storage.datasource.RatePairLocalDataSourceImpl
import io.github.viacheslav.chugunov.moniqo.storage.datasource.SettingLocalDataSource
import io.github.viacheslav.chugunov.moniqo.storage.datasource.SettingLocalDataSourceImpl
import io.github.viacheslav.chugunov.moniqo.storage.db.AppDatabase
import io.github.viacheslav.chugunov.moniqo.storage.mapper.CurrencyStorageMapper
import io.github.viacheslav.chugunov.moniqo.storage.mapper.CurrencyStorageMapperImpl
import io.github.viacheslav.chugunov.moniqo.storage.mapper.RatePairStorageMapper
import io.github.viacheslav.chugunov.moniqo.storage.mapper.RatePairStorageMapperImpl
import io.github.viacheslav.chugunov.moniqo.storage.platform.DatabaseDriverFactory
import io.github.viacheslav.chugunov.moniqo.storage.repository.CurrencyStorageRepositoryImpl
import io.github.viacheslav.chugunov.moniqo.storage.repository.RatePairStorageRepositoryImpl
import io.github.viacheslav.chugunov.moniqo.storage.repository.SettingStorageRepositoryImpl
import org.koin.dsl.module

val storageModule =
    module {
        factory<SqlDriver> {
            get<DatabaseDriverFactory>().create()
        }

        single<AppDatabase> {
            AppDatabase(get())
        }

        factory<CurrencyStorageMapper> {
            CurrencyStorageMapperImpl()
        }

        factory<RatePairStorageMapper> {
            RatePairStorageMapperImpl()
        }

        factory<CurrencyLocalDataSource> {
            CurrencyLocalDataSourceImpl(get(), get())
        }

        factory<CurrencyFallbackDataSource> {
            CurrencyFallbackDataSourceImpl(get(), get())
        }

        factory<RatePairLocalDataSource> {
            RatePairLocalDataSourceImpl(get(), get())
        }

        factory<SettingLocalDataSource> {
            SettingLocalDataSourceImpl(get(), get())
        }

        factory<RatePairFallbackDataSource> {
            RatePairFallbackDataSourceImpl(get(), get())
        }

        single<CurrencyStorageRepository> {
            CurrencyStorageRepositoryImpl(get(), get(), get())
        }

        single<RatePairStorageRepository> {
            RatePairStorageRepositoryImpl(get(), get(), get())
        }

        single<SettingStorageRepository> {
            SettingStorageRepositoryImpl(get())
        }
    }
