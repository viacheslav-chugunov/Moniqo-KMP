package io.github.viacheslav.chugunov.moniqo.ui.core.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.viacheslav.chugunov.moniqo.android.ui.core.R
import io.github.viacheslav.chugunov.moniqo.ui.core.ComponentPreview
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.MoniqoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBarComponent(
    onRatesClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.home_title),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        actions = {
            TextButton(onClick = onRatesClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_trending_up),
                    contentDescription = null,
                    modifier = Modifier.padding(end = 4.dp),
                )
                Text(text = stringResource(R.string.home_rates), fontWeight = FontWeight.Medium)
            }
            IconButton(onClick = onSettingsClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_settings),
                    contentDescription = stringResource(R.string.cd_settings),
                )
            }
        },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
    )
}

@ComponentPreview
@Composable
private fun HomeTopBarComponentPreview() {
    MoniqoTheme {
        HomeTopBarComponent(
            onRatesClick = {},
            onSettingsClick = {},
        )
    }
}
