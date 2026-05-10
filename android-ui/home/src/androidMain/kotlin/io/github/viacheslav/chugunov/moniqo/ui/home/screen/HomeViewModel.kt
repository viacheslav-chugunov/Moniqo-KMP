package io.github.viacheslav.chugunov.moniqo.ui.home.screen

import androidx.lifecycle.viewModelScope
import io.github.viacheslav.chugunov.moniqo.core.model.RatePair
import io.github.viacheslav.chugunov.moniqo.core.usecase.GetRatePairFlowUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.SaveFromRateUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.SaveToRateUseCase
import io.github.viacheslav.chugunov.moniqo.ui.core.AppViewModel
import kotlinx.coroutines.launch
import kotlin.text.iterator

internal class HomeViewModel(
    private val getRatePairFlowUseCase: GetRatePairFlowUseCase,
    private val saveFromRateUseCase: SaveFromRateUseCase,
    private val saveToRateUseCase: SaveToRateUseCase,
    private val mapper: HomeMapper,
) : AppViewModel<HomeState, HomeIntent, HomeEffect>(HomeState.Loading) {
    private var ratePair: RatePair? = null

    init {
        viewModelScope.launch {
            getRatePairFlowUseCase().collect { pair ->
                ratePair = pair
                updateState { current ->
                    val fromAmount = (current as? HomeState.Content)?.fromAmount ?: ""
                    mapper.toHomeContent(pair, fromAmount)
                }
            }
        }
    }

    override fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.ChangeFromAmount -> handleChangeFromAmount(intent.input)
            is HomeIntent.ChangeToAmount -> handleChangeToAmount(intent.input)
            is HomeIntent.SwapCurrencies -> handleSwapCurrencies()
        }
    }

    private fun handleChangeFromAmount(input: String) {
        val sanitized = sanitize(input)
        val pair = ratePair
        updateContent { current ->
            if (pair == null) {
                current.copy(fromAmount = sanitized)
            } else {
                mapper.toHomeContent(pair, sanitized)
            }
        }
    }

    private fun handleChangeToAmount(input: String) {
        val sanitized = sanitize(input)
        val pair = ratePair
        updateContent { current ->
            current.copy(
                toAmount = sanitized,
                analysis =
                    mapper.toExchangeAnalysis(
                        current.fromAmount,
                        sanitized,
                        pair?.let { mapper.toOfficialRate(it) },
                        current.fromCurrency,
                        current.toCurrency,
                    ),
            )
        }
    }

    private fun handleSwapCurrencies() {
        val pair = ratePair ?: return
        viewModelScope.launch {
            saveFromRateUseCase(pair.toRate)
            saveToRateUseCase(pair.fromRate)
        }
    }

    private fun updateContent(block: (HomeState.Content) -> HomeState.Content) {
        updateState { if (it is HomeState.Content) block(it) else it }
    }

    private fun sanitize(input: String): String =
        buildString {
            var hasDot = false
            for (char in input) {
                when {
                    char.isDigit() -> append(char)
                    char == '.' && !hasDot -> {
                        hasDot = true
                        append(char)
                    }
                }
            }
        }
}
