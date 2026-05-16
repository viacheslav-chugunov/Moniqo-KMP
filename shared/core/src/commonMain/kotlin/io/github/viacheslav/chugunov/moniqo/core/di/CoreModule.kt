package io.github.viacheslav.chugunov.moniqo.core.di

import io.github.viacheslav.chugunov.moniqo.core.usecase.FetchCurrencyRatesUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.GetAppLanguageFlowUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.GetAppThemeFlowUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.GetBaseRateCurrencyFlowUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.GetCurrencyRatesUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.GetDealRangesFlowUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.GetRatePairFlowUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.ResetDealRangesUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.SaveFromRateUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.SaveToRateUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.SetAppLanguageUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.SetAppThemeUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.SetBaseRatesCurrencyUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.SetDealRangesUseCase
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
            FetchCurrencyRatesUseCase(get(), get())
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

        factory {
            GetAppLanguageFlowUseCase(get())
        }

        factory {
            SetAppLanguageUseCase(get())
        }

        factory {
            GetAppThemeFlowUseCase(get())
        }

        factory {
            SetAppThemeUseCase(get())
        }

        factory {
            GetDealRangesFlowUseCase(get())
        }

        factory {
            ResetDealRangesUseCase(get())
        }

        factory {
            SetDealRangesUseCase(get())
        }

        factory {
            GetBaseRateCurrencyFlowUseCase(get())
        }

        factory {
            SetBaseRatesCurrencyUseCase(get())
        }
    }
