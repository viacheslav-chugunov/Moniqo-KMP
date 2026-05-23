package io.github.viacheslav.chugunov.moniqo.ui.core.extensions

import androidx.compose.runtime.Composable
import dev.icerock.moko.resources.compose.stringResource
import io.github.viacheslav.chugunov.moniqo.core.MR
import io.github.viacheslav.chugunov.moniqo.core.model.AppTheme

@Composable
internal fun AppTheme.toLabel(): String =
    when (this) {
        AppTheme.LIGHT -> stringResource(MR.strings.settings_theme_light)
        AppTheme.DARK -> stringResource(MR.strings.settings_theme_dark)
        AppTheme.SYSTEM -> stringResource(MR.strings.settings_theme_system)
    }
