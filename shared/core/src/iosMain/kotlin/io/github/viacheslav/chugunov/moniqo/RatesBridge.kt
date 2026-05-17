package io.github.viacheslav.chugunov.moniqo

import io.github.viacheslav.chugunov.moniqo.core.model.Currency
import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyRates
import io.github.viacheslav.chugunov.moniqo.core.usecase.FetchCurrencyRatesUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.GetBaseRateCurrencyFlowUseCase
import kotlinx.coroutines.launch
import org.koin.core.component.get

fun fetchRates(onLoaded: (CurrencyRates) -> Unit): () -> Unit {
    val job = iosCoroutineScope.launch {
        val rates = IosKoin.get<FetchCurrencyRatesUseCase>()()
        onLoaded(rates)
    }
    return { job.cancel() }
}

fun observeBaseCurrency(onUpdate: (Currency) -> Unit): () -> Unit {
    val job = iosCoroutineScope.launch {
        IosKoin.get<GetBaseRateCurrencyFlowUseCase>()().collect(onUpdate)
    }
    return { job.cancel() }
}
