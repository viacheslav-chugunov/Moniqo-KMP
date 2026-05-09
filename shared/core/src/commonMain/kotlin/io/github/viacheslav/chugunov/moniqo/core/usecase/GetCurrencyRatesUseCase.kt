package io.github.viacheslav.chugunov.moniqo.core.usecase

import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyRates
import io.github.viacheslav.chugunov.moniqo.core.repository.CurrencyNetworkRepository
import io.github.viacheslav.chugunov.moniqo.core.repository.CurrencyStorageRepository

class GetCurrencyRatesUseCase(
    private val storageRepository: CurrencyStorageRepository,
    private val networkRepository: CurrencyNetworkRepository,
) {
    suspend operator fun invoke(): CurrencyRates =
        runCatching {
            networkRepository.getRates()
                .also { rates -> storageRepository.save(rates) }
        }.getOrDefault(storageRepository.getCache())
}
