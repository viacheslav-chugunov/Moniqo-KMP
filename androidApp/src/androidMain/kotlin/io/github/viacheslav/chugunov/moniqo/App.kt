package io.github.viacheslav.chugunov.moniqo

import androidx.compose.runtime.Composable
import io.github.viacheslav.chugunov.moniqo.ui.choosecurrency.screen.ChooseCurrencyScreen
import io.github.viacheslav.chugunov.moniqo.ui.core.navigation.AppNavigation
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.MoniqoTheme
import io.github.viacheslav.chugunov.moniqo.ui.home.screen.HomeScreen
import io.github.viacheslav.chugunov.moniqo.ui.rates.screen.RatesScreen
import io.github.viacheslav.chugunov.moniqo.ui.settings.SettingsScreen

@Composable
fun App() {
    MoniqoTheme {
        AppNavigation(
            homeScreen = ::HomeScreen,
            ratesScreen = ::RatesScreen,
            chooseCurrencyScreen = ::ChooseCurrencyScreen,
            settingsScreen = ::SettingsScreen,
        )
    }
}
