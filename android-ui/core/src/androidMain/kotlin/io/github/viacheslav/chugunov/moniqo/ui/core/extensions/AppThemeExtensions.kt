package io.github.viacheslav.chugunov.moniqo.ui.core.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.github.viacheslav.chugunov.moniqo.android.ui.core.R
import io.github.viacheslav.chugunov.moniqo.core.model.AppTheme

@Composable
internal fun AppTheme.toLabel(): String =
    when (this) {
        AppTheme.LIGHT -> stringResource(R.string.settings_theme_light)
        AppTheme.DARK -> stringResource(R.string.settings_theme_dark)
        AppTheme.SYSTEM -> stringResource(R.string.settings_theme_system)
    }
