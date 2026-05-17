package io.github.viacheslav.chugunov.moniqo

import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyRates
import io.github.viacheslav.chugunov.moniqo.core.model.Rate
import io.github.viacheslav.chugunov.moniqo.core.usecase.AddRecentCurrencyUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.GetCurrencyRatesUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.GetRecentCurrenciesFlowUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.SaveFromRateUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.SaveToRateUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.SetBaseRatesCurrencyUseCase
import kotlinx.coroutines.launch
import org.koin.core.component.get

fun loadCurrencyRates(onLoaded: (CurrencyRates) -> Unit): () -> Unit {
    val job = iosCoroutineScope.launch {
        val rates = IosKoin.get<GetCurrencyRatesUseCase>()()
        onLoaded(rates)
    }
    return { job.cancel() }
}

fun observeRecentCurrencies(onUpdate: (List<String>) -> Unit): () -> Unit {
    val job = iosCoroutineScope.launch {
        IosKoin.get<GetRecentCurrenciesFlowUseCase>()().collect(onUpdate)
    }
    return { job.cancel() }
}

fun saveFromRateByCode(rates: CurrencyRates, code: String): () -> Unit {
    val job = iosCoroutineScope.launch {
        val rate = rates.findRate(code) ?: return@launch
        IosKoin.get<SaveFromRateUseCase>()(rate)
    }
    return { job.cancel() }
}

fun saveToRateByCode(rates: CurrencyRates, code: String): () -> Unit {
    val job = iosCoroutineScope.launch {
        val rate = rates.findRate(code) ?: return@launch
        IosKoin.get<SaveToRateUseCase>()(rate)
    }
    return { job.cancel() }
}

fun setBaseCurrencyByCode(rates: CurrencyRates, code: String): () -> Unit {
    val job = iosCoroutineScope.launch {
        val currency = rates.findRate(code)?.currency ?: return@launch
        IosKoin.get<SetBaseRatesCurrencyUseCase>()(currency)
    }
    return { job.cancel() }
}

fun addRecentCurrency(code: String): () -> Unit {
    val job = iosCoroutineScope.launch {
        IosKoin.get<AddRecentCurrencyUseCase>()(code)
    }
    return { job.cancel() }
}

private fun CurrencyRates.findRate(code: String): Rate? {
    val upper = code.uppercase()
    if (baseCurrency.name.uppercase() == upper) return Rate(baseCurrency, 1.0)
    return rates.find { it.currency.name.uppercase() == upper }
}
