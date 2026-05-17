package io.github.viacheslav.chugunov.moniqo

import io.github.viacheslav.chugunov.moniqo.core.model.Rate
import io.github.viacheslav.chugunov.moniqo.core.model.RatePair
import io.github.viacheslav.chugunov.moniqo.core.usecase.GetRatePairFlowUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.SaveFromRateUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.SaveToRateUseCase
import kotlinx.coroutines.launch
import org.koin.core.component.get

fun observeRatePair(onUpdate: (RatePair) -> Unit): () -> Unit {
    val job = iosCoroutineScope.launch {
        IosKoin.get<GetRatePairFlowUseCase>()().collect(onUpdate)
    }
    return { job.cancel() }
}

fun swapRates(fromRate: Rate, toRate: Rate): () -> Unit {
    val job = iosCoroutineScope.launch {
        IosKoin.get<SaveFromRateUseCase>()(fromRate)
        IosKoin.get<SaveToRateUseCase>()(toRate)
    }
    return { job.cancel() }
}
