package io.github.viacheslav.chugunov.moniqo.ui.core

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.viacheslav.chugunov.moniqo.ui.core.model.CurrencyInfo
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.MoniqoTheme

@Composable
fun CurrencyListItemComponent(
    currency: CurrencyInfo,
    trailingText: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    val clickableModifier = if (onClick != null) modifier.clickable(onClick = onClick) else modifier
    Row(
        modifier = clickableModifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(text = currency.flag, fontSize = 32.sp)
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = currency.code,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = currency.name,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = trailingText,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@ComponentPreview
@Composable
private fun CurrencyListItemComponentPreview() {
    MoniqoTheme {
        CurrencyListItemComponent(
            currency = CurrencyInfo("USD", "US Dollar", "🇺🇸", isCrypto = false),
            trailingText = "$",
            onClick = {},
        )
    }
}
