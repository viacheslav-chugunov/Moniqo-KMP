package io.github.viacheslav.chugunov.moniqo.ui.choosecurrency.di

import io.github.viacheslav.chugunov.moniqo.ui.choosecurrency.screen.ChooseCurrencyMapper
import io.github.viacheslav.chugunov.moniqo.ui.choosecurrency.screen.ChooseCurrencyMapperImpl
import io.github.viacheslav.chugunov.moniqo.ui.choosecurrency.screen.ChooseCurrencyViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val chooseCurrencyModule =
    module {
        factory<ChooseCurrencyMapper> { ChooseCurrencyMapperImpl(get()) }
        viewModelOf(::ChooseCurrencyViewModel)
    }
