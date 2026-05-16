package io.github.viacheslav.chugunov.moniqo.ui.home.screen

import androidx.lifecycle.viewModelScope
import io.github.viacheslav.chugunov.moniqo.core.model.DealRanges
import io.github.viacheslav.chugunov.moniqo.core.model.RatePair
import io.github.viacheslav.chugunov.moniqo.core.usecase.GetDealRangesFlowUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.GetRatePairFlowUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.SaveFromRateUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.SaveToRateUseCase
import io.github.viacheslav.chugunov.moniqo.ui.core.AppViewModel
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.text.iterator

internal class HomeViewModel(
    private val getRatePairFlowUseCase: GetRatePairFlowUseCase,
    private val saveFromRateUseCase: SaveFromRateUseCase,
    private val saveToRateUseCase: SaveToRateUseCase,
    private val getDealRangesFlowUseCase: GetDealRangesFlowUseCase,
    private val mapper: HomeMapper,
) : AppViewModel<HomeState, HomeIntent, HomeEffect>(HomeState.Loading) {
    private var currentRatePair: RatePair? = null
    private var currentDealRanges: DealRanges? = null

    init {
        combineTransform<DealRanges, RatePair, HomeState>(
            getDealRangesFlowUseCase(),
            getRatePairFlowUseCase()
        ) { dealRanges, ratePair ->
            currentRatePair = ratePair
            currentDealRanges = dealRanges
            emit(
                mapper.toHomeState(
                    ratePair = ratePair,
                    dealRanges = dealRanges,
                    currentContentState = childState()
                )
            )
        }.onEach { state ->
            updateState { state }
        }.launchIn(viewModelScope)
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
        val pair = currentRatePair
        val dealRanges = currentDealRanges ?: return
        updateContent { current ->
            if (pair == null) {
                current.copy(fromAmount = sanitized)
            } else {
                val content = childState<HomeState.Content>()?.copy(fromAmount = input)
                mapper.toHomeState(pair, dealRanges, content)
            }
        }
    }

    private fun handleChangeToAmount(input: String) {
        val sanitized = sanitize(input)
        val pair = currentRatePair
        val dealRanges = currentDealRanges ?: return
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
                        dealRanges,
                    ),
            )
        }
    }

    private fun handleSwapCurrencies() {
        val pair = currentRatePair ?: return
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
