package io.github.viacheslav.chugunov.moniqo.ui.core

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Light",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    name = "Dark",
    showBackground = true,
    backgroundColor = 0xFF1F2937,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    name = "Landscape",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    name = "Large Text",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    fontScale = 1.5f,
)
annotation class ComponentPreview

@Preview(
    name = "Light",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = "spec:width=360dp,height=800dp,dpi=420",
)
@Preview(
    name = "Dark",
    showBackground = true,
    backgroundColor = 0xFF1F2937,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=360dp,height=800dp,dpi=420",
)
@Preview(
    name = "Landscape",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    widthDp = 800,
    heightDp = 800,
)
@Preview(
    name = "Large Text",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    fontScale = 1.5f,
    device = "spec:width=360dp,height=800dp,dpi=420",
)
annotation class ScreenPreview
