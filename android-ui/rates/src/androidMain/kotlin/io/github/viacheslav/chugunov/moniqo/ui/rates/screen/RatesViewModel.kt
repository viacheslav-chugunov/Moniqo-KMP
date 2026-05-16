package io.github.viacheslav.chugunov.moniqo.ui.rates.screen

import androidx.lifecycle.viewModelScope
import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyRates
import io.github.viacheslav.chugunov.moniqo.core.usecase.FetchCurrencyRatesUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.GetCurrencyRatesUseCase
import io.github.viacheslav.chugunov.moniqo.ui.core.AppViewModel
import io.github.viacheslav.chugunov.moniqo.ui.core.RatesBaseCurrencyHolder
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

internal class RatesViewModel(
    private val getCurrencyRatesUseCase: GetCurrencyRatesUseCase,
    private val fetchCurrencyRatesUseCase: FetchCurrencyRatesUseCase,
    private val mapper: RatesMapper,
    private val baseCurrencyHolder: RatesBaseCurrencyHolder,
) : AppViewModel<RatesState, RatesIntent, RatesEffect>(RatesState.Loading) {

    private var currencyRates: CurrencyRates? = null

    init {
        viewModelScope.launch {
            val rates = getCurrencyRatesUseCase()
            currencyRates = rates
            // Apply any already-selected base currency (covers ViewModel re-creation after navigation)
            val viewBase = baseCurrencyHolder.currency.value
            updateState { mapper.toContent(rates, viewBase) }
        }
        viewModelScope.launch {
            baseCurrencyHolder.currency.filterNotNull().collect { viewBase ->
                val rates = currencyRates ?: return@collect
                updateState { current ->
                    val content = current as? RatesState.Content
                    mapper.toContent(rates, viewBase).copy(
                        query = content?.query ?: "",
                        filter = content?.filter ?: RatesFilter.All,
                    )
                }
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
        val current = state.value as? RatesState.Content ?: return
        if (current.isRefreshing) return
        updateState { if (it is RatesState.Content) it.copy(isRefreshing = true) else it }
        viewModelScope.launch {
            val rates = fetchCurrencyRatesUseCase()
            currencyRates = rates
            val viewBase = baseCurrencyHolder.currency.value
            updateState { prev ->
                val content = prev as? RatesState.Content
                mapper.toContent(rates, viewBase).copy(
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
