package io.github.viacheslav.chugunov.moniqo.ui.home.screen

sealed interface HomeIntent {
    data class ChangeFromAmount(val input: String) : HomeIntent

    data class ChangeToAmount(val input: String) : HomeIntent

    data object SwapCurrencies : HomeIntent
}
