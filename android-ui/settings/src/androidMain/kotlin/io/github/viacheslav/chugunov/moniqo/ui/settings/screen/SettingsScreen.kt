package io.github.viacheslav.chugunov.moniqo.ui.settings.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.viacheslav.chugunov.moniqo.android.ui.core.R
import io.github.viacheslav.chugunov.moniqo.core.model.AppLanguage
import io.github.viacheslav.chugunov.moniqo.core.model.AppTheme
import io.github.viacheslav.chugunov.moniqo.ui.core.ScreenPreview
import io.github.viacheslav.chugunov.moniqo.ui.core.component.FullscreenLoading
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.MoniqoTheme
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.badRed
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.goodGreen
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.mediumAmber
import io.github.viacheslav.chugunov.moniqo.ui.settings.component.AppearanceSectionComponent
import io.github.viacheslav.chugunov.moniqo.ui.settings.component.DealRangeRowComponent
import io.github.viacheslav.chugunov.moniqo.ui.settings.component.DealRangesSectionComponent
import io.github.viacheslav.chugunov.moniqo.ui.settings.component.EditRangesBottomSheetComponent
import io.github.viacheslav.chugunov.moniqo.ui.settings.component.LanguageSectionComponent
import io.github.viacheslav.chugunov.moniqo.ui.settings.component.PickLanguageBottomSheetComponent
import io.github.viacheslav.chugunov.moniqo.ui.settings.component.SettingsSectionComponent
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val viewModel = koinViewModel<SettingsViewModel>()

    when (val state = viewModel.state.collectAsStateWithLifecycle().value) {
        is SettingsState.Content -> {
            SettingsScreenContent(
                state = state,
                onBack = onBack,
                onThemeChange = { viewModel.onIntent(SettingsIntent.ChangeTheme(it)) },
                onApplyRanges = { good, average ->
                    viewModel.onIntent(SettingsIntent.ChangeDealRanges(good, average))
                },
                onResetRanges = { viewModel.onIntent(SettingsIntent.ResetRanges) },
                onOpenRangeEditor = { viewModel.onIntent(SettingsIntent.OpenRangeEditor) },
                onCloseRangeEditor = { viewModel.onIntent(SettingsIntent.CloseRangeEditor) },
                onOpenLanguagePicker = { viewModel.onIntent(SettingsIntent.OpenLanguagePicker) },
                onCloseLanguagePicker = { viewModel.onIntent(SettingsIntent.CloseLanguagePicker) },
                onLanguageChange = { viewModel.onIntent(SettingsIntent.ChangeLanguage(it)) },
            )
        }
        SettingsState.Loading -> {
            FullscreenLoading()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreenContent(
    state: SettingsState.Content,
    onBack: () -> Unit,
    onThemeChange: (AppTheme) -> Unit,
    onApplyRanges: (Float, Float) -> Unit,
    onResetRanges: () -> Unit,
    onOpenRangeEditor: () -> Unit,
    onCloseRangeEditor: () -> Unit,
    onOpenLanguagePicker: () -> Unit,
    onCloseLanguagePicker: () -> Unit,
    onLanguageChange: (AppLanguage) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.settings_title),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back),
                        )
                    }
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            AppearanceSectionComponent(themeMode = state.theme, onThemeChange = onThemeChange)
            LanguageSectionComponent(currentLanguage = state.language, onPickLanguage = onOpenLanguagePicker)
            DealRangesSectionComponent(
                goodMax = state.goodDealMaxPercent,
                mediumMax = state.mediumDealMaxPercent,
                onEdit = onOpenRangeEditor,
                onReset = onResetRanges,
            )
            Text(
                text = stringResource(R.string.settings_footer),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 20.dp),
            )
        }
    }

    if (state.isEditingRanges) {
        EditRangesBottomSheetComponent(
            initialGoodMax = state.goodDealMaxPercent,
            initialAverageMax = state.mediumDealMaxPercent,
            onApply = onApplyRanges,
            onDismiss = onCloseRangeEditor,
        )
    }

    if (state.isPickingLanguage) {
        PickLanguageBottomSheetComponent(
            currentLanguage = state.language,
            onLanguageChange = onLanguageChange,
            onDismiss = onCloseLanguagePicker,
        )
    }
}

@ScreenPreview
@Composable
private fun SettingsScreenContentPreview() {
    MoniqoTheme {
        SettingsScreenContent(
            state = SettingsState.Content.PREVIEW,
            onBack = {},
            onThemeChange = {},
            onApplyRanges = { _, _ -> },
            onResetRanges = {},
            onOpenRangeEditor = {},
            onCloseRangeEditor = {},
            onOpenLanguagePicker = {},
            onCloseLanguagePicker = {},
            onLanguageChange = {},
        )
    }
}
