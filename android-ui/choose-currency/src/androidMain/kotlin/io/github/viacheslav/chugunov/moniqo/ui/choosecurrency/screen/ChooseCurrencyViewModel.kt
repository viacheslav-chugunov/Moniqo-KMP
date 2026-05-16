package io.github.viacheslav.chugunov.moniqo.ui.choosecurrency.screen

import androidx.lifecycle.viewModelScope
import io.github.viacheslav.chugunov.moniqo.core.model.Currency
import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyFilter
import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyRates
import io.github.viacheslav.chugunov.moniqo.core.model.Rate
import io.github.viacheslav.chugunov.moniqo.core.usecase.GetCurrencyRatesUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.GetRatePairFlowUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.SaveFromRateUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.SaveToRateUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.SetBaseRatesCurrencyUseCase
import io.github.viacheslav.chugunov.moniqo.ui.core.AppViewModel
import io.github.viacheslav.chugunov.moniqo.ui.core.navigation.CurrencySlot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

internal class ChooseCurrencyViewModel(
    private val getCurrencyRatesUseCase: GetCurrencyRatesUseCase,
    private val getRatePairFlowUseCase: GetRatePairFlowUseCase,
    private val saveFromRateUseCase: SaveFromRateUseCase,
    private val saveToRateUseCase: SaveToRateUseCase,
    private val setRatesBaseCurrencyUseCase: SetBaseRatesCurrencyUseCase,
    private val mapper: ChooseCurrencyMapper,
) : AppViewModel<ChooseCurrencyState, ChooseCurrencyIntent, ChooseCurrencyEffect>(ChooseCurrencyState.Loading) {
    private var currencyRates: CurrencyRates? = null

    init {
        viewModelScope.launch {
            val rates = getCurrencyRatesUseCase()
            val pair = getRatePairFlowUseCase().first()
            currencyRates = rates
            updateState { mapper.toContent(rates, pair) }
        }
    }

    override fun onIntent(intent: ChooseCurrencyIntent) {
        when (intent) {
            is ChooseCurrencyIntent.Search -> handleSearch(intent.query)
            is ChooseCurrencyIntent.Filter -> handleFilter(intent.filter)
            is ChooseCurrencyIntent.SelectCurrency -> handleSelectCurrency(intent.currency, intent.slot)
        }
    }

    private fun handleSearch(query: String) {
        updateState { if (it is ChooseCurrencyState.Content) it.copy(query = query) else it }
    }

    private fun handleFilter(filter: CurrencyFilter) {
        updateState { if (it is ChooseCurrencyState.Content) it.copy(filter = filter) else it }
    }

    private fun handleSelectCurrency(
        currency: io.github.viacheslav.chugunov.moniqo.core.model.CurrencyInfo,
        slot: CurrencySlot,
    ) {
        viewModelScope.launch {
            val rates = currencyRates
            when (slot) {
                CurrencySlot.FROM -> {
                    val rate = rates?.findRate(currency.code) ?: return@launch
                    saveFromRateUseCase(rate)
                }
                CurrencySlot.TO -> {
                    val rate = rates?.findRate(currency.code) ?: return@launch
                    saveToRateUseCase(rate)
                }
                CurrencySlot.BASE -> {
                    viewModelScope.launch {
                        setRatesBaseCurrencyUseCase(Currency.of(currency.code))
                    }
                }
            }
            updateState { if (it is ChooseCurrencyState.Content) it.copy(query = "") else it }
            sendEffect(ChooseCurrencyEffect.NavigateBack)
        }
    }

    private fun CurrencyRates.findRate(code: String): Rate? {
        if (baseCurrency.name.uppercase() == code) {
            return Rate(baseCurrency, 1.0)
        }
        return rates.find { it.currency.name.uppercase() == code }
    }
}
