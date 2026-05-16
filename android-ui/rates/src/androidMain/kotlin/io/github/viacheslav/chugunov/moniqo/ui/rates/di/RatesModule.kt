package io.github.viacheslav.chugunov.moniqo.ui.rates.di

import io.github.viacheslav.chugunov.moniqo.ui.rates.screen.RatesMapper
import io.github.viacheslav.chugunov.moniqo.ui.rates.screen.RatesMapperImpl
import io.github.viacheslav.chugunov.moniqo.ui.rates.screen.RatesViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val ratesModule =
    module {
        factory<RatesMapper> { RatesMapperImpl(get()) }
        viewModelOf(::RatesViewModel)
    }
