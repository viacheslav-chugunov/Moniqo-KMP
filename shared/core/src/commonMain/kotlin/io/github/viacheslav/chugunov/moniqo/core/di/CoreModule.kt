package io.github.viacheslav.chugunov.moniqo.core.di

import io.github.viacheslav.chugunov.moniqo.core.usecase.GetCurrencyRatesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module

val coreModule = module {
    single<CoroutineDispatchers> {
        CoroutineDispatchers(
            io = Dispatchers.IO,
            default = Dispatchers.Default,
            main = Dispatchers.Main,
        )
    }

    factory {
        GetCurrencyRatesUseCase(get(), get())
    }
}
