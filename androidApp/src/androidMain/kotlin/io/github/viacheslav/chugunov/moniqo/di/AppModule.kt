package io.github.viacheslav.chugunov.moniqo.di

import io.github.viacheslav.chugunov.moniqo.screen.AppMapper
import io.github.viacheslav.chugunov.moniqo.screen.AppMapperImpl
import io.github.viacheslav.chugunov.moniqo.screen.AppViewModel
import io.github.viacheslav.chugunov.moniqo.storage.platform.DatabaseDriverFactory
import io.github.viacheslav.chugunov.moniqo.ui.core.StringProvider
import io.github.viacheslav.chugunov.moniqo.ui.core.StringProviderImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule =
    module {
        single<StringProvider> {
            StringProviderImpl(androidContext())
        }

        factory {
            DatabaseDriverFactory(androidContext())
        }

        viewModelOf(::AppViewModel)

        factory<AppMapper> {
            AppMapperImpl()
        }
    }
