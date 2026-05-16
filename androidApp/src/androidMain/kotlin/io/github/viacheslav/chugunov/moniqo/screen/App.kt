package io.github.viacheslav.chugunov.moniqo.screen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.viacheslav.chugunov.moniqo.core.model.AppTheme
import io.github.viacheslav.chugunov.moniqo.ui.choosecurrency.screen.ChooseCurrencyScreen
import io.github.viacheslav.chugunov.moniqo.ui.core.component.FullscreenLoading
import io.github.viacheslav.chugunov.moniqo.ui.core.navigation.AppNavigation
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.MoniqoTheme
import io.github.viacheslav.chugunov.moniqo.ui.home.screen.HomeScreen
import io.github.viacheslav.chugunov.moniqo.ui.rates.screen.RatesScreen
import io.github.viacheslav.chugunov.moniqo.ui.settings.screen.SettingsScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun App() {
    val viewModel = koinViewModel<AppViewModel>()

    when (val state = viewModel.state.collectAsStateWithLifecycle().value) {
        is AppState.Content -> {
            val darkTheme =
                when (state.theme) {
                    AppTheme.LIGHT -> false
                    AppTheme.DARK -> true
                    AppTheme.SYSTEM -> isSystemInDarkTheme()
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
        AppState.Loading -> {
            FullscreenLoading()
        }
    }
}
