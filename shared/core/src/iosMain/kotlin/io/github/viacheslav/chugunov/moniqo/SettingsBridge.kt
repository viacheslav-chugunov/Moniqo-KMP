package io.github.viacheslav.chugunov.moniqo

import io.github.viacheslav.chugunov.moniqo.core.model.AppLanguage
import io.github.viacheslav.chugunov.moniqo.core.model.AppTheme
import io.github.viacheslav.chugunov.moniqo.core.model.DealRanges
import io.github.viacheslav.chugunov.moniqo.core.usecase.GetAppLanguageFlowUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.GetAppThemeFlowUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.GetDealRangesFlowUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.ResetDealRangesUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.SetAppLanguageUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.SetAppThemeUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.SetDealRangesUseCase
import kotlinx.coroutines.launch
import org.koin.core.component.get

fun observeAppTheme(onUpdate: (AppTheme) -> Unit): () -> Unit {
    val job =
        iosCoroutineScope.launch {
            IosKoin.get<GetAppThemeFlowUseCase>()().collect(onUpdate)
        }
    return { job.cancel() }
}

fun setAppTheme(theme: AppTheme): () -> Unit {
    val job = iosCoroutineScope.launch {
        IosKoin.get<SetAppThemeUseCase>()(theme)
    }
    return { job.cancel() }
}

fun observeAppLanguage(onUpdate: (AppLanguage) -> Unit): () -> Unit {
    val job =
        iosCoroutineScope.launch {
            IosKoin.get<GetAppLanguageFlowUseCase>()().collect(onUpdate)
        }
    return { job.cancel() }
}

fun setAppLanguage(language: AppLanguage): () -> Unit {
    val job = iosCoroutineScope.launch {
        IosKoin.get<SetAppLanguageUseCase>()(language)
    }
    return { job.cancel() }
}

fun observeDealRanges(onUpdate: (DealRanges) -> Unit): () -> Unit {
    val job =
        iosCoroutineScope.launch {
            IosKoin.get<GetDealRangesFlowUseCase>()().collect(onUpdate)
        }
    return { job.cancel() }
}

fun setDealRanges(
    good: Int,
    medium: Int,
): () -> Unit {
    val job = iosCoroutineScope.launch {
        IosKoin.get<SetDealRangesUseCase>()(DealRanges(good = good, medium = medium))
    }
    return { job.cancel() }
}

fun resetDealRanges(): () -> Unit {
    val job = iosCoroutineScope.launch {
        IosKoin.get<ResetDealRangesUseCase>()()
    }
    return { job.cancel() }
}
