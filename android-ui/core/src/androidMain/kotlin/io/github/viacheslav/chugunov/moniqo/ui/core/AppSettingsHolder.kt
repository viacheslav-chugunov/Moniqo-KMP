package io.github.viacheslav.chugunov.moniqo.ui.core

import io.github.viacheslav.chugunov.moniqo.ui.core.model.DealRanges
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class AppSettings(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val dealRanges: DealRanges = DealRanges(),
)

class AppSettingsHolder {
    private val _settings = MutableStateFlow(AppSettings())
    val settings: StateFlow<AppSettings> = _settings

    fun updateTheme(mode: ThemeMode) {
        _settings.update { it.copy(themeMode = mode) }
    }

    fun updateDealRanges(ranges: DealRanges) {
        _settings.update { it.copy(dealRanges = ranges) }
    }
}
