package io.github.viacheslav.chugunov.moniqo.ui.choosecurrency.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import io.github.viacheslav.chugunov.moniqo.core.MR
import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyFilter
import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyInfo
import io.github.viacheslav.chugunov.moniqo.ui.core.ScreenPreview
import io.github.viacheslav.chugunov.moniqo.ui.core.component.CurrencyListItemComponent
import io.github.viacheslav.chugunov.moniqo.ui.core.model.CurrencyMeta
import io.github.viacheslav.chugunov.moniqo.ui.core.navigation.CurrencySlot
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.MoniqoTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseCurrencyScreen(
    slot: CurrencySlot,
    onBack: () -> Unit,
) {
    val viewModel = koinViewModel<ChooseCurrencyViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                ChooseCurrencyEffect.NavigateBack -> onBack()
            }
        }
    }

    ChooseCurrencyScreenContent(
        state = state,
        onBack = onBack,
        onSearch = { viewModel.onIntent(ChooseCurrencyIntent.Search(it)) },
        onFilter = { viewModel.onIntent(ChooseCurrencyIntent.Filter(it)) },
        onSelectCurrency = { currency ->
            viewModel.onIntent(ChooseCurrencyIntent.SelectCurrency(currency, slot))
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChooseCurrencyScreenContent(
    state: ChooseCurrencyState,
    onBack: () -> Unit,
    onSearch: (String) -> Unit,
    onFilter: (CurrencyFilter) -> Unit,
    onSelectCurrency: (CurrencyInfo) -> Unit,
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
                            query = (state as? ChooseCurrencyState.Content)?.query ?: "",
                            onQueryChange = onSearch,
                        )
                    } else {
                        Text(
                            text = stringResource(MR.strings.choose_currency_title),
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
                            painter = painterResource(io.github.viacheslav.chugunov.moniqo.android.ui.core.R.drawable.ic_arrow_back),
                            contentDescription = stringResource(io.github.viacheslav.chugunov.moniqo.core.MR.strings.cd_back),
                        )
                    }
                },
                actions = {
                    if (!isSearchActive) {
                        IconButton(onClick = { isSearchActive = true }) {
                            Icon(
                                painter = painterResource(io.github.viacheslav.chugunov.moniqo.android.ui.core.R.drawable.ic_search),
                                contentDescription =
                                    stringResource(
                                        io.github.viacheslav.chugunov.moniqo.core.MR.strings.cd_search,
                                    ),
                            )
                        }
                    }
                },
            )
        },
    ) { innerPadding ->
        when (state) {
            is ChooseCurrencyState.Loading -> {
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
            is ChooseCurrencyState.Content -> {
                ChooseCurrencyContent(
                    state = state,
                    modifier = Modifier.padding(innerPadding),
                    onFilter = onFilter,
                    onSelectCurrency = onSelectCurrency,
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
                text = stringResource(MR.strings.choose_currency_search_hint),
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
private fun ChooseCurrencyContent(
    state: ChooseCurrencyState.Content,
    modifier: Modifier = Modifier,
    onFilter: (CurrencyFilter) -> Unit,
    onSelectCurrency: (CurrencyInfo) -> Unit,
) {
    val grouped = state.groupedCurrencies
    val recent = state.displayedRecent

    LazyColumn(
        modifier =
            modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp),
    ) {
        item {
            FilterRow(
                filter = state.filter,
                onFilterChange = onFilter,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }
        if (recent.isNotEmpty()) {
            item {
                SectionHeader(
                    text = stringResource(MR.strings.choose_currency_recent),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }
            itemsIndexed(recent) { index, currency ->
                CurrencyListItemComponent(
                    currency = currency,
                    trailingText = CurrencyMeta.symbols[currency.code] ?: currency.code,
                    onClick = { onSelectCurrency(currency) },
                )
                if (index < recent.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.outlineVariant,
                    )
                }
            }
        }
        item {
            SectionHeader(
                text = stringResource(MR.strings.choose_currency_all),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }
        grouped.forEach { (letter, currencies) ->
            item(key = "letter_$letter") {
                Text(
                    text = letter.toString(),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                )
            }
            itemsIndexed(
                items = currencies,
                key = { _, item -> "item_${item.code}" },
            ) { index, currency ->
                CurrencyListItemComponent(
                    currency = currency,
                    trailingText = CurrencyMeta.symbols[currency.code] ?: currency.code,
                    onClick = { onSelectCurrency(currency) },
                )
                if (index < currencies.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.outlineVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterRow(
    filter: CurrencyFilter,
    onFilterChange: (CurrencyFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        CurrencyFilter.entries.forEach { option ->
            val label =
                when (option) {
                    CurrencyFilter.All -> stringResource(MR.strings.choose_currency_filter_all)
                    CurrencyFilter.Fiat -> stringResource(MR.strings.choose_currency_filter_fiat)
                    CurrencyFilter.Crypto -> stringResource(MR.strings.choose_currency_filter_crypto)
                }
            FilterChip(
                selected = filter == option,
                onClick = { onFilterChange(option) },
                label = { Text(label) },
            )
        }
    }
}

@Composable
private fun SectionHeader(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier,
    )
}

@ScreenPreview
@Composable
private fun ChooseCurrencyScreenContentPreview() {
    MoniqoTheme {
        ChooseCurrencyScreenContent(
            state = ChooseCurrencyState.Content.PREVIEW,
            onBack = {},
            onSearch = {},
            onFilter = {},
            onSelectCurrency = {},
        )
    }
}
