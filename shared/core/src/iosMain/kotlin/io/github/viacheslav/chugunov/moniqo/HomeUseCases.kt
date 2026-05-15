package io.github.viacheslav.chugunov.moniqo

import io.github.viacheslav.chugunov.moniqo.core.model.RatePair
import io.github.viacheslav.chugunov.moniqo.core.usecase.GetRatePairFlowUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.SaveFromRateUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.SaveToRateUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

private object HomeKoin : KoinComponent

fun observeRatePair(onUpdate: (RatePair) -> Unit): () -> Unit {
    val scope = CoroutineScope(Dispatchers.Main)
    scope.launch {
        HomeKoin.get<GetRatePairFlowUseCase>()().collect(onUpdate)
    }
    return { scope.cancel() }
}

fun swapCurrencies() {
    CoroutineScope(Dispatchers.Main).launch {
        val useCase = HomeKoin.get<GetRatePairFlowUseCase>()
        val current = useCase().first()
        HomeKoin.get<SaveFromRateUseCase>()(current.toRate)
        HomeKoin.get<SaveToRateUseCase>()(current.fromRate)
    }
}
