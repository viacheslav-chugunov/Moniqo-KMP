package io.github.viacheslav.chugunov.moniqo.ui.core.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.github.viacheslav.chugunov.moniqo.android.ui.core.R
import io.github.viacheslav.chugunov.moniqo.core.model.AppTheme
import io.github.viacheslav.chugunov.moniqo.ui.core.ComponentPreview
import io.github.viacheslav.chugunov.moniqo.ui.core.extensions.toLabel
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.MoniqoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearanceSectionComponent(
    themeMode: AppTheme,
    onThemeChange: (AppTheme) -> Unit,
) {
    SettingsSectionComponent(title = stringResource(R.string.settings_appearance)) {
        val themes = AppTheme.entries
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            themes.forEachIndexed { index, theme ->
                SegmentedButton(
                    selected = themeMode == theme,
                    onClick = { onThemeChange(theme) },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = themes.size),
                    label = { Text(theme.toLabel()) },
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun AppearanceSectionPreview() {
    MoniqoTheme {
        AppearanceSectionComponent(themeMode = AppTheme.SYSTEM, onThemeChange = {})
    }
}
