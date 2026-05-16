package io.github.viacheslav.chugunov.moniqo.ui.choosecurrency.screen

internal sealed interface ChooseCurrencyEffect {
    data object NavigateBack : ChooseCurrencyEffect
}
