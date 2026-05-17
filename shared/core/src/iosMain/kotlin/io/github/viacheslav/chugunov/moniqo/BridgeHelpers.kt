package io.github.viacheslav.chugunov.moniqo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.component.KoinComponent

internal val iosCoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

internal object IosKoin : KoinComponent
