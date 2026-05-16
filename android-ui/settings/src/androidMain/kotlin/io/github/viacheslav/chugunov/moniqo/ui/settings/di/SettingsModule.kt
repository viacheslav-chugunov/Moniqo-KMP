package io.github.viacheslav.chugunov.moniqo.ui.settings.di

import io.github.viacheslav.chugunov.moniqo.ui.settings.screen.SettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val settingsModule =
    module {
        viewModelOf(::SettingsViewModel)
    }
