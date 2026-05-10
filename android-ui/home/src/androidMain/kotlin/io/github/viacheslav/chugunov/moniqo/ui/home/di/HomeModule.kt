package io.github.viacheslav.chugunov.moniqo.ui.home.di

import io.github.viacheslav.chugunov.moniqo.ui.home.screen.HomeMapper
import io.github.viacheslav.chugunov.moniqo.ui.home.screen.HomeMapperImpl
import io.github.viacheslav.chugunov.moniqo.ui.home.screen.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homeModule =
    module {
        factory<HomeMapper> { HomeMapperImpl(get()) }
        viewModelOf(::HomeViewModel)
    }
