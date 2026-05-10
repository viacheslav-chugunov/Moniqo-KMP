package io.github.viacheslav.chugunov.moniqo.di

import io.github.viacheslav.chugunov.moniqo.storage.platform.DatabaseDriverFactory
import io.github.viacheslav.chugunov.moniqo.ui.core.StringProvider
import io.github.viacheslav.chugunov.moniqo.ui.core.StringProviderImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule =
    module {
        single<StringProvider> {
            StringProviderImpl(androidContext())
        }

        factory {
            DatabaseDriverFactory(androidContext())
        }
    }
