package io.github.viacheslav.chugunov.moniqo.core.usecase

import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyRates
import io.github.viacheslav.chugunov.moniqo.core.repository.CurrencyNetworkRepository
import io.github.viacheslav.chugunov.moniqo.core.repository.CurrencyStorageRepository

/**
 * Returns the current currency rates from local storage.
 *
 * If storage is empty, fetches the latest rates from the network, caches them, and returns
 * the result. If the network request also fails, the built-in fallback dataset is returned.
 *
 * **Error handling:** No wrapping required at the call site. The call always returns
 * a valid [CurrencyRates] result.
 */
class GetCurrencyRatesUseCase(
    private val storageRepository: CurrencyStorageRepository,
    private val networkRepository: CurrencyNetworkRepository,
) {
    suspend operator fun invoke(): CurrencyRates =
        if (storageRepository.isEmpty()) {
            runCatching {
                networkRepository.getRates()
                    .also { storageRepository.saveRates(it) }
            }.getOrDefault(storageRepository.getRates())
        } else {
            storageRepository.getRates()
        }
}
