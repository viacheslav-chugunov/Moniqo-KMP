package io.github.viacheslav.chugunov.moniqo.ui.home.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.viacheslav.chugunov.moniqo.ui.core.ScreenPreview
import io.github.viacheslav.chugunov.moniqo.ui.core.component.AmountSectionComponent
import io.github.viacheslav.chugunov.moniqo.ui.core.component.CurrencyPairSectionComponent
import io.github.viacheslav.chugunov.moniqo.ui.core.component.EmptyStateHintComponent
import io.github.viacheslav.chugunov.moniqo.ui.core.component.ExchangeAnalysisCardComponent
import io.github.viacheslav.chugunov.moniqo.ui.core.component.FullscreenLoading
import io.github.viacheslav.chugunov.moniqo.ui.core.component.HomeTopBarComponent
import io.github.viacheslav.chugunov.moniqo.ui.core.navigation.AppRoute
import io.github.viacheslav.chugunov.moniqo.ui.core.navigation.CurrencySlot
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.MoniqoTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(onNavigate: (AppRoute) -> Unit) {
    val viewModel = koinViewModel<HomeViewModel>()

    when (val state = viewModel.state.collectAsStateWithLifecycle().value) {
        is HomeState.Content -> {
            HomeScreenContent(
                state = state,
                onFromAmountChange = { viewModel.onIntent(HomeIntent.ChangeFromAmount(it)) },
                onToAmountChange = { viewModel.onIntent(HomeIntent.ChangeToAmount(it)) },
                onSwapClick = { viewModel.onIntent(HomeIntent.SwapCurrencies) },
                onFromCurrencyClick = { onNavigate(AppRoute.ChooseCurrency(CurrencySlot.FROM)) },
                onToCurrencyClick = { onNavigate(AppRoute.ChooseCurrency(CurrencySlot.TO)) },
                onRatesClick = { onNavigate(AppRoute.Rates) },
                onSettingsClick = { onNavigate(AppRoute.Settings) },
            )
        }
        HomeState.Loading -> {
            FullscreenLoading()
        }
    }
}

@Composable
private fun HomeScreenContent(
    state: HomeState.Content,
    modifier: Modifier = Modifier,
    onFromAmountChange: (String) -> Unit,
    onToAmountChange: (String) -> Unit,
    onSwapClick: () -> Unit,
    onFromCurrencyClick: () -> Unit,
    onToCurrencyClick: () -> Unit,
    onRatesClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            HomeTopBarComponent(onRatesClick = onRatesClick, onSettingsClick = onSettingsClick)
        },
    ) { padding ->
        Column(
            modifier =
                modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            CurrencyPairSectionComponent(
                fromCurrency = state.fromCurrency,
                toCurrency = state.toCurrency,
                onFromClick = onFromCurrencyClick,
                onToClick = onToCurrencyClick,
                onSwapClick = onSwapClick,
            )
            AmountSectionComponent(
                fromCurrency = state.fromCurrency,
                toCurrency = state.toCurrency,
                fromAmount = state.fromAmount,
                toAmount = state.toAmount,
                fromHint = state.fromHint,
                toHint = state.toHint,
                onFromAmountChange = onFromAmountChange,
                onToAmountChange = onToAmountChange,
            )
            if (state.analysis != null) {
                ExchangeAnalysisCardComponent(
                    analysis = state.analysis,
                )
            } else {
                EmptyStateHintComponent()
            }
        }
    }
}

@ScreenPreview
@Composable
private fun HomeScreenScreenContentPreview() {
    MoniqoTheme {
        HomeScreenContent(
            state = HomeState.Content.PREVIEW,
            onFromAmountChange = {},
            onToAmountChange = {},
            onSwapClick = {},
            onFromCurrencyClick = {},
            onToCurrencyClick = {},
            onRatesClick = {},
            onSettingsClick = {},
        )
    }
}
