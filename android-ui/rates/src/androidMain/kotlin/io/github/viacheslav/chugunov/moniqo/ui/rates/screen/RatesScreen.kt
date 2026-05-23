package io.github.viacheslav.chugunov.moniqo.ui.rates.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.icerock.moko.resources.compose.stringResource
import io.github.viacheslav.chugunov.moniqo.android.ui.core.R
import io.github.viacheslav.chugunov.moniqo.core.MR
import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyFilter
import io.github.viacheslav.chugunov.moniqo.ui.core.ScreenPreview
import io.github.viacheslav.chugunov.moniqo.ui.core.component.BaseCurrencySelectorComponent
import io.github.viacheslav.chugunov.moniqo.ui.core.component.CurrencyListItemComponent
import io.github.viacheslav.chugunov.moniqo.ui.core.navigation.AppRoute
import io.github.viacheslav.chugunov.moniqo.ui.core.navigation.CurrencySlot
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.MoniqoTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatesScreen(
    onBack: () -> Unit,
    onNavigate: (AppRoute) -> Unit,
) {
    val viewModel = koinViewModel<RatesViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    RatesScreenContent(
        state = state,
        onBack = onBack,
        onRefresh = { viewModel.onIntent(RatesIntent.Refresh) },
        onSearch = { viewModel.onIntent(RatesIntent.Search(it)) },
        onFilter = { viewModel.onIntent(RatesIntent.Filter(it)) },
        onBaseCurrencyClick = { onNavigate(AppRoute.ChooseCurrency(CurrencySlot.BASE)) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RatesScreenContent(
    state: RatesState,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    onSearch: (String) -> Unit,
    onFilter: (CurrencyFilter) -> Unit,
    onBaseCurrencyClick: () -> Unit = {},
) {
    var isSearchActive by remember { mutableStateOf(false) }

    BackHandler(enabled = isSearchActive) {
        isSearchActive = false
        onSearch("")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isSearchActive) {
                        SearchField(
                            query = (state as? RatesState.Content)?.query ?: "",
                            onQueryChange = onSearch,
                        )
                    } else {
                        Text(
                            text = stringResource(MR.strings.rates_title),
                            fontWeight = FontWeight.Bold,
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (isSearchActive) {
                            isSearchActive = false
                            onSearch("")
                        } else {
                            onBack()
                        }
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = stringResource(MR.strings.cd_back),
                        )
                    }
                },
                actions = {
                    if (!isSearchActive) {
                        IconButton(onClick = { isSearchActive = true }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_search),
                                contentDescription = stringResource(MR.strings.cd_search),
                            )
                        }
                    }
                },
            )
        },
    ) { innerPadding ->
        when (state) {
            is RatesState.Loading -> {
                Box(
                    modifier =
                        Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
            is RatesState.Content -> {
                RatesContent(
                    state = state,
                    modifier = Modifier.padding(innerPadding),
                    onRefresh = onRefresh,
                    onFilter = onFilter,
                    onBaseCurrencyClick = onBaseCurrencyClick,
                )
            }
        }
    }
}

@Composable
private fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    Box(contentAlignment = Alignment.CenterStart) {
        if (query.isEmpty()) {
            Text(
                text = stringResource(MR.strings.rates_search_hint),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
            singleLine = true,
            textStyle =
                MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        )
    }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }
}

@Composable
private fun RatesContent(
    state: RatesState.Content,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,
    onFilter: (CurrencyFilter) -> Unit,
    onBaseCurrencyClick: () -> Unit = {},
) {
    val displayedRates = state.displayedRates

    LazyColumn(
        modifier =
            modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            Text(
                text = stringResource(MR.strings.rates_base_currency),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        item {
            BaseCurrencySelectorComponent(
                currency = state.baseCurrency,
                onClick = onBaseCurrencyClick,
            )
        }
        item {
            UpdateRow(
                updatedAt = state.updatedAt,
                isRefreshing = state.isRefreshing,
                onRefresh = onRefresh,
            )
        }
        item {
            FilterRow(filter = state.filter, onFilterChange = onFilter)
        }
        item {
            Text(
                text = stringResource(MR.strings.rates_exchange_rates),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        if (displayedRates.isEmpty()) {
            item {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(MR.strings.rates_no_results, state.query),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        } else {
            itemsIndexed(displayedRates) { index, rate ->
                Column {
                    CurrencyListItemComponent(currency = rate.currency, trailingText = rate.rate)
                    if (index < displayedRates.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            color = MaterialTheme.colorScheme.outlineVariant,
                        )
                    }
                }
            }
        }
        item {
            Text(
                text = stringResource(MR.strings.rates_disclaimer),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun FilterRow(
    filter: CurrencyFilter,
    onFilterChange: (CurrencyFilter) -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        CurrencyFilter.entries.forEach { option ->
            val labelRes =
                when (option) {
                    CurrencyFilter.All -> MR.strings.rates_filter_all
                    CurrencyFilter.Fiat -> MR.strings.rates_filter_fiat
                    CurrencyFilter.Crypto -> MR.strings.rates_filter_crypto
                }
            FilterChip(
                selected = filter == option,
                onClick = { onFilterChange(option) },
                label = { Text(stringResource(labelRes)) },
            )
        }
    }
}

@Composable
private fun UpdateRow(
    updatedAt: String,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_access_time),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp),
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = updatedAt,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f),
        )
        RefreshButton(isRefreshing = isRefreshing, onClick = onRefresh)
    }
}

@Composable
private fun RefreshButton(
    isRefreshing: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.primaryContainer,
        onClick = { if (!isRefreshing) onClick() },
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            if (isRefreshing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(14.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 2.dp,
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.ic_refresh),
                    contentDescription = stringResource(MR.strings.cd_refresh),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(14.dp),
                )
            }
            Text(
                text = stringResource(MR.strings.rates_refresh),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@ScreenPreview
@Composable
private fun RatesScreenContentPreview() {
    MoniqoTheme {
        RatesScreenContent(
            state = RatesState.Content.PREVIEW,
            onBack = {},
            onRefresh = {},
            onSearch = {},
            onFilter = {},
            onBaseCurrencyClick = {},
        )
    }
}

@ScreenPreview
@Composable
private fun RatesScreenSearchPreview() {
    MoniqoTheme {
        RatesScreenContent(
            state = RatesState.Content.PREVIEW.copy(query = "usd"),
            onBack = {},
            onRefresh = {},
            onSearch = {},
            onFilter = {},
            onBaseCurrencyClick = {},
        )
    }
}

@ScreenPreview
@Composable
private fun RatesScreenCryptoFilterPreview() {
    MoniqoTheme {
        RatesScreenContent(
            state = RatesState.Content.PREVIEW.copy(filter = CurrencyFilter.Crypto),
            onBack = {},
            onRefresh = {},
            onSearch = {},
            onFilter = {},
            onBaseCurrencyClick = {},
        )
    }
}

@ScreenPreview
@Composable
private fun RatesScreenLoadingPreview() {
    MoniqoTheme {
        RatesScreenContent(
            state = RatesState.Loading,
            onBack = {},
            onRefresh = {},
            onSearch = {},
            onFilter = {},
            onBaseCurrencyClick = {},
        )
    }
}
