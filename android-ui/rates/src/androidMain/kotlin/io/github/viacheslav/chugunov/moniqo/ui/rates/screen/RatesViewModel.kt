package io.github.viacheslav.chugunov.moniqo.ui.rates.screen

import androidx.lifecycle.viewModelScope
import io.github.viacheslav.chugunov.moniqo.core.usecase.FetchCurrencyRatesUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.GetCurrencyRatesUseCase
import io.github.viacheslav.chugunov.moniqo.ui.core.AppViewModel
import kotlinx.coroutines.launch

internal class RatesViewModel(
    private val getCurrencyRatesUseCase: GetCurrencyRatesUseCase,
    private val fetchCurrencyRatesUseCase: FetchCurrencyRatesUseCase,
    private val mapper: RatesMapper,
) : AppViewModel<RatesState, RatesIntent, RatesEffect>(RatesState.Loading) {

    init {
        viewModelScope.launch {
            val rates = getCurrencyRatesUseCase()
            updateState { mapper.toContent(rates) }
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
        val current = state.value as? RatesState.Content ?: return
        if (current.isRefreshing) return
        updateState { if (it is RatesState.Content) it.copy(isRefreshing = true) else it }
        viewModelScope.launch {
            val rates = fetchCurrencyRatesUseCase()
            updateState { prev ->
                val content = prev as? RatesState.Content
                mapper.toContent(rates).copy(
                    query = content?.query ?: "",
                    filter = content?.filter ?: RatesFilter.All,
                )
            }
        }
    }

    private fun handleSearch(query: String) {
        updateState { if (it is RatesState.Content) it.copy(query = query) else it }
    }

    private fun handleFilter(filter: RatesFilter) {
        updateState { if (it is RatesState.Content) it.copy(filter = filter) else it }
    }
}
