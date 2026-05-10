package io.github.viacheslav.chugunov.moniqo.ui.home.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.viacheslav.chugunov.moniqo.android.ui.core.R
import io.github.viacheslav.chugunov.moniqo.ui.core.ComponentPreview
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.MoniqoTheme

@Composable
fun SwapButtonComponent(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilledTonalIconButton(
        onClick = onClick,
        modifier = modifier.size(44.dp),
        shape = CircleShape,
        colors =
            IconButtonDefaults.filledTonalIconButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ),
    ) {
        Icon(
            imageVector = Icons.Default.SwapHoriz,
            contentDescription = stringResource(R.string.cd_swap_currencies),
        )
    }
}

@ComponentPreview
@Composable
private fun SwapButtonComponentPreview() {
    MoniqoTheme {
        SwapButtonComponent(onClick = {})
    }
}
