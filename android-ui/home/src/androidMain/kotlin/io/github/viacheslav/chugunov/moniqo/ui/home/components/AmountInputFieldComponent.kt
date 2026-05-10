package io.github.viacheslav.chugunov.moniqo.ui.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.viacheslav.chugunov.moniqo.android.ui.core.R
import io.github.viacheslav.chugunov.moniqo.ui.core.ComponentPreview
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.MoniqoTheme
import io.github.viacheslav.chugunov.moniqo.ui.home.model.CurrencyInfo

@Composable
internal fun AmountInputFieldComponent(
    currency: CurrencyInfo,
    amount: String,
    hint: String,
    isActive: Boolean,
    onAmountChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val labelColor =
        if (isActive) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
    val amountColor =
        if (amount.isEmpty()) {
            MaterialTheme.colorScheme.onSurfaceVariant
        } else {
            MaterialTheme.colorScheme.onSurface
        }
    val maxFontSize = MaterialTheme.typography.headlineLarge.fontSize
    val minFontSize = 14.sp
    val amountStyle =
        MaterialTheme.typography.headlineLarge.copy(
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.End,
        )
    val textMeasurer = rememberTextMeasurer()

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = currency.code,
                style = MaterialTheme.typography.labelLarge,
                color = labelColor,
                modifier = Modifier.width(52.dp),
            )
            BoxWithConstraints(modifier = Modifier.weight(1f)) {
                val containerWidth = constraints.maxWidth
                val fontSize =
                    remember(amount, containerWidth) {
                        val displayText = formatAmountForDisplay(amount)
                        if (containerWidth <= 0 || displayText.isEmpty()) return@remember maxFontSize
                        val measured =
                            textMeasurer.measure(
                                text = displayText,
                                style = amountStyle.copy(fontSize = maxFontSize),
                                softWrap = false,
                                maxLines = 1,
                            )
                        val scale =
                            if (measured.size.width > 0) {
                                containerWidth.toFloat() / measured.size.width
                            } else {
                                1f
                            }
                        if (scale >= 1f) {
                            maxFontSize
                        } else {
                            val scaled = maxFontSize * scale
                            if (scaled.value < minFontSize.value) minFontSize else scaled
                        }
                    }
                BasicTextField(
                    value = amount,
                    onValueChange = { if (it.length <= 16) onAmountChange(it) },
                    textStyle = amountStyle.copy(color = amountColor, fontSize = fontSize),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    visualTransformation = ThousandsSeparatorVisualTransformation,
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.CenterEnd,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            if (amount.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.amount_placeholder),
                                    style = amountStyle,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            innerTextField()
                        }
                    },
                )
            }
        }
        if (hint.isNotEmpty()) {
            Spacer(Modifier.height(4.dp))
            RateHintComponent(
                text = hint,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

private fun formatAmountForDisplay(raw: String): String {
    if (raw.isEmpty()) return raw
    val dotIndex = raw.indexOf('.')
    val intPart = if (dotIndex >= 0) raw.substring(0, dotIndex) else raw
    val decPart = if (dotIndex >= 0) raw.substring(dotIndex) else ""
    if (intPart.isEmpty()) return raw
    val formatted =
        buildString {
            val len = intPart.length
            intPart.forEachIndexed { i, c ->
                if (i > 0 && (len - i) % 3 == 0) append(',')
                append(c)
            }
        }
    return formatted + decPart
}

private object ThousandsSeparatorVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val raw = text.text
        val formatted = formatAmountForDisplay(raw)
        val dotIndex = raw.indexOf('.')
        val intLen = if (dotIndex >= 0) dotIndex else raw.length
        val mapping =
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    val p = minOf(offset, intLen)
                    val commas = (intLen - 1) / 3 - maxOf(0, intLen - p - 1) / 3
                    return offset + commas
                }

                override fun transformedToOriginal(offset: Int): Int {
                    var commas = 0
                    for (i in 0 until minOf(offset, formatted.length)) {
                        if (formatted[i] == ',') commas++
                    }
                    return offset - commas
                }
            }
        return TransformedText(AnnotatedString(formatted), mapping)
    }
}

@ComponentPreview
@Composable
private fun AmountInputFieldActivePreview() {
    MoniqoTheme {
        AmountInputFieldComponent(
            currency = CurrencyInfo("EUR", "Euro", "🇪🇺"),
            amount = "1000.00",
            hint = "Official: 1 EUR = 1.1732 USD",
            isActive = true,
            onAmountChange = {},
        )
    }
}

@ComponentPreview
@Composable
private fun AmountInputFieldPassivePreview() {
    MoniqoTheme {
        AmountInputFieldComponent(
            currency = CurrencyInfo("USD", "US Dollar", "🇺🇸"),
            amount = "1170.00",
            hint = "At official rate ≈ 1,173.20 USD",
            isActive = false,
            onAmountChange = {},
        )
    }
}
