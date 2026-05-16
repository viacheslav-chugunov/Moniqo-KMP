package io.github.viacheslav.chugunov.moniqo.ui.core.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.viacheslav.chugunov.moniqo.android.ui.core.R
import io.github.viacheslav.chugunov.moniqo.core.model.DealQuality
import io.github.viacheslav.chugunov.moniqo.ui.core.ComponentPreview
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.MoniqoTheme
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.badRed
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.badRedContainer
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.goodGreen
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.goodGreenContainer
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.mediumAmber
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.mediumAmberContainer

@Composable
internal fun DealBadgeComponent(
    quality: DealQuality,
    modifier: Modifier = Modifier,
) {
    val backgroundColor =
        when (quality) {
            DealQuality.GOOD -> MaterialTheme.colorScheme.goodGreenContainer
            DealQuality.MEDIUM -> MaterialTheme.colorScheme.mediumAmberContainer
            DealQuality.BAD -> MaterialTheme.colorScheme.badRedContainer
        }
    val contentColor =
        when (quality) {
            DealQuality.GOOD -> MaterialTheme.colorScheme.goodGreen
            DealQuality.MEDIUM -> MaterialTheme.colorScheme.mediumAmber
            DealQuality.BAD -> MaterialTheme.colorScheme.badRed
        }
    val label =
        when (quality) {
            DealQuality.GOOD -> stringResource(R.string.deal_quality_good)
            DealQuality.MEDIUM -> stringResource(R.string.deal_quality_medium)
            DealQuality.BAD -> stringResource(R.string.deal_quality_bad)
        }
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Canvas(modifier = Modifier.size(6.dp)) {
                drawCircle(color = contentColor)
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = contentColor,
            )
        }
    }
}

@ComponentPreview
@Composable
private fun DealBadgeGoodPreview() {
    MoniqoTheme { DealBadgeComponent(DealQuality.GOOD) }
}

@ComponentPreview
@Composable
private fun DealBadgeMediumPreview() {
    MoniqoTheme { DealBadgeComponent(DealQuality.MEDIUM) }
}

@ComponentPreview
@Composable
private fun DealBadgeBadPreview() {
    MoniqoTheme { DealBadgeComponent(DealQuality.BAD) }
}
