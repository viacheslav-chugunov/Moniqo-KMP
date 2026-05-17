package io.github.viacheslav.chugunov.moniqo

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.component.KoinComponent

internal val iosCoroutineScope = CoroutineScope(
    Dispatchers.Main + SupervisorJob() + CoroutineExceptionHandler { _, throwable ->
        println("Bridge error: $throwable")
    }
)

internal object IosKoin : KoinComponent
