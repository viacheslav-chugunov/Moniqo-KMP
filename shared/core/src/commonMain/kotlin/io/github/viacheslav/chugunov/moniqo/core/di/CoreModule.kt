package io.github.viacheslav.chugunov.moniqo.core.di

import io.github.viacheslav.chugunov.moniqo.core.usecase.GetCurrencyRatesUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.GetRatePairFlowUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.SaveFromRateUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.SaveToRateUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val coreModule =
    module {
        single<Json> {
            Json {
                ignoreUnknownKeys = true
                isLenient = true
            }
        }

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

        factory {
            GetRatePairFlowUseCase(get())
        }

        factory {
            SaveFromRateUseCase(get())
        }

        factory {
            SaveToRateUseCase(get())
        }
    }
