package io.github.viacheslav.chugunov.moniqo.ui.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay

@Composable
fun AppNavigation(
    homeScreen: @Composable (onNavigate: (AppRoute) -> Unit) -> Unit,
    ratesScreen: @Composable (onBack: () -> Unit, onNavigate: (AppRoute) -> Unit) -> Unit,
    chooseCurrencyScreen: @Composable (slot: CurrencySlot, onBack: () -> Unit) -> Unit,
    settingsScreen: @Composable (onBack: () -> Unit) -> Unit,
) {
    val backStack = rememberNavBackStack(AppRoute.Home)
    val navigator = remember(backStack) { SafeNavigator(backStack) }

    NavDisplay(
        backStack = backStack,
        onBack = navigator::goBack,
        entryProvider =
            entryProvider {
                entry<AppRoute.Home> { homeScreen(navigator::navigate) }
                entry<AppRoute.Rates> { ratesScreen(navigator::goBack, navigator::navigate) }
                entry<AppRoute.ChooseCurrency> { chooseCurrencyScreen(it.slot, navigator::goBack) }
                entry<AppRoute.Settings> { settingsScreen(navigator::goBack) }
            },
    )
}
