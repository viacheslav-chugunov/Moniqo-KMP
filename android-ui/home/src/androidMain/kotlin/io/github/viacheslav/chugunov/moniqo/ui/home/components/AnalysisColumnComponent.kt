package io.github.viacheslav.chugunov.moniqo.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.viacheslav.chugunov.moniqo.ui.core.ComponentPreview
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.MoniqoTheme
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.goodGreen

@Composable
internal fun AnalysisColumnComponent(
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = valueColor,
        )
    }
}

@ComponentPreview
@Composable
private fun AnalysisColumnComponentPreview() {
    MoniqoTheme {
        AnalysisColumnComponent(
            label = "Official rate",
            value = "1 EUR = 1.17 USD",
            valueColor = MaterialTheme.colorScheme.goodGreen,
        )
    }
}
