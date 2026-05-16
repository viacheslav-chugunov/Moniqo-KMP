package io.github.viacheslav.chugunov.moniqo.ui.settings.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.github.viacheslav.chugunov.moniqo.android.ui.core.R
import io.github.viacheslav.chugunov.moniqo.core.model.AppLanguage

@Composable
internal fun AppLanguage.displayName(): String =
    when (this) {
        AppLanguage.SYSTEM -> stringResource(R.string.settings_language_system)
        AppLanguage.ENGLISH -> stringResource(R.string.settings_language_english_native)
        AppLanguage.RUSSIAN -> stringResource(R.string.settings_language_russian_native)
        AppLanguage.LATVIAN -> stringResource(R.string.settings_language_latvian_native)
    }
