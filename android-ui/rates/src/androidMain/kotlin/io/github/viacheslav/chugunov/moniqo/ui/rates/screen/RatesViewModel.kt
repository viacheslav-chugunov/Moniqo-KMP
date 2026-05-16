package io.github.viacheslav.chugunov.moniqo.ui.rates.screen

import androidx.lifecycle.viewModelScope
import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyFilter
import io.github.viacheslav.chugunov.moniqo.core.usecase.FetchCurrencyRatesUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.GetBaseRateCurrencyFlowUseCase
import io.github.viacheslav.chugunov.moniqo.ui.core.AppViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal class RatesViewModel(
    private val fetchCurrencyRatesUseCase: FetchCurrencyRatesUseCase,
    private val getBaseRateCurrencyFlowUseCase: GetBaseRateCurrencyFlowUseCase,
    private val mapper: RatesMapper,
) : AppViewModel<RatesState, RatesIntent, RatesEffect>(RatesState.Loading) {
    init {
        viewModelScope.launch {
            val currencyRates = fetchCurrencyRatesUseCase()
            getBaseRateCurrencyFlowUseCase().collectLatest { currency ->
                updateState { mapper.toRatesState(currencyRates, currency, childState()) }
            }
        }
    }

    override fun onIntent(intent: RatesIntent) {
        when (intent) {
            RatesIntent.Refresh -> handleRefresh()
            is RatesIntent.Search -> handleSearch(intent.query)
            is RatesIntent.Filter -> handleFilter(intent.filter)
        }
    }

    private fun handleRefresh() {
        if (childState<RatesState.Content>()?.isRefreshing == true) return
        updateChildState<RatesState.Content> { it.copy(isRefreshing = true) }
        viewModelScope.launch {
            val currencyRates = fetchCurrencyRatesUseCase()
            getBaseRateCurrencyFlowUseCase().collectLatest { currency ->
                updateState {
                    val content = childState<RatesState.Content>()?.copy(isRefreshing = false)
                    mapper.toRatesState(currencyRates, currency, content)
                }
            }
        }
    }

    private fun handleSearch(query: String) {
        updateState { if (it is RatesState.Content) it.copy(query = query) else it }
    }

    private fun handleFilter(filter: CurrencyFilter) {
        updateState { if (it is RatesState.Content) it.copy(filter = filter) else it }
    }
}
