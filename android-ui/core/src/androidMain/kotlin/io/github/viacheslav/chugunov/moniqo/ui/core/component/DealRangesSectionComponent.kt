package io.github.viacheslav.chugunov.moniqo.ui.core.component

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.icerock.moko.resources.compose.stringResource
import io.github.viacheslav.chugunov.moniqo.core.MR
import io.github.viacheslav.chugunov.moniqo.ui.core.ComponentPreview
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.MoniqoTheme
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.badRed
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.goodGreen
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.mediumAmber

@Composable
fun DealRangesSectionComponent(
    goodMax: Float,
    mediumMax: Float,
    onEdit: () -> Unit,
    onReset: () -> Unit,
) {
    SettingsSectionComponent(
        title = stringResource(MR.strings.settings_deal_ranges),
        trailingAction = {
            TextButton(onClick = onEdit) {
                Text(
                    text = stringResource(MR.strings.settings_edit_ranges),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        },
    ) {
        DealRangeRowComponent(
            label = stringResource(MR.strings.settings_good_deal),
            value =
                remember(goodMax) {
                    "0–${goodMax.toInt()}%"
                },
            color = MaterialTheme.colorScheme.goodGreen,
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        DealRangeRowComponent(
            label = stringResource(MR.strings.settings_medium_deal),
            value =
                remember(goodMax, mediumMax) {
                    "${goodMax.toInt()}–${mediumMax.toInt()}%"
                },
            color = MaterialTheme.colorScheme.mediumAmber,
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        DealRangeRowComponent(
            label = stringResource(MR.strings.settings_bad_deal),
            value =
                remember(mediumMax) {
                    "${mediumMax.toInt()}%+"
                },
            color = MaterialTheme.colorScheme.badRed,
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        TextButton(
            onClick = onReset,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            Text(
                text = stringResource(MR.strings.settings_reset_ranges),
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@ComponentPreview
@Composable
private fun DealRangesSectionPreview() {
    MoniqoTheme {
        DealRangesSectionComponent(goodMax = 5f, mediumMax = 15f, onEdit = {}, onReset = {})
    }
}
