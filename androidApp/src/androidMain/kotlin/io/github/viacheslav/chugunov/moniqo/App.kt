package io.github.viacheslav.chugunov.moniqo

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.viacheslav.chugunov.moniqo.ui.choosecurrency.screen.ChooseCurrencyScreen
import io.github.viacheslav.chugunov.moniqo.ui.core.AppSettingsHolder
import io.github.viacheslav.chugunov.moniqo.ui.core.navigation.AppNavigation
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.MoniqoTheme
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.ThemeMode
import io.github.viacheslav.chugunov.moniqo.ui.home.screen.HomeScreen
import io.github.viacheslav.chugunov.moniqo.ui.rates.screen.RatesScreen
import io.github.viacheslav.chugunov.moniqo.ui.settings.screen.SettingsScreen
import org.koin.compose.koinInject

@Composable
fun App() {
    val appSettingsHolder = koinInject<AppSettingsHolder>()
    val settings by appSettingsHolder.settings.collectAsStateWithLifecycle()
    val darkTheme =
        when (settings.themeMode) {
            ThemeMode.LIGHT -> false
            ThemeMode.DARK -> true
            ThemeMode.SYSTEM -> isSystemInDarkTheme()
        }

    MoniqoTheme(darkTheme = darkTheme) {
        AppNavigation(
            homeScreen = ::HomeScreen,
            ratesScreen = ::RatesScreen,
            chooseCurrencyScreen = ::ChooseCurrencyScreen,
            settingsScreen = ::SettingsScreen,
        )
    }
}
