package io.github.viacheslav.chugunov.moniqo.core.usecase

import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyRates
import io.github.viacheslav.chugunov.moniqo.core.repository.CurrencyNetworkRepository
import io.github.viacheslav.chugunov.moniqo.core.repository.CurrencyStorageRepository

/**
 * Fetches the latest currency rates from the network and caches them in local storage.
 *
 * If the network request fails for any reason (no connectivity, server error, etc.),
 * the use case transparently falls back to the most recently cached rates. If no cache
 * exists, the built-in fallback dataset is returned instead.
 *
 * **Error handling:** No wrapping required at the call site — failures are handled
 * internally via [runCatching]. The call always returns a valid [CurrencyRates] result.
 */
class GetCurrencyRatesUseCase(
    private val storageRepository: CurrencyStorageRepository,
    private val networkRepository: CurrencyNetworkRepository,
) {
    suspend operator fun invoke(): CurrencyRates =
        runCatching {
            networkRepository.getRates()
                .also { rates -> storageRepository.saveRates(rates) }
        }.getOrDefault(storageRepository.getRates())
}
