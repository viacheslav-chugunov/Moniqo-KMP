package io.github.viacheslav.chugunov.moniqo.core.usecase

import io.github.viacheslav.chugunov.moniqo.core.model.Rate
import io.github.viacheslav.chugunov.moniqo.core.repository.RatePairStorageRepository
import kotlinx.coroutines.flow.first

/**
 * Saves a new "to" rate, replacing the second currency in the currently selected pair.
 *
 * Reads the current [RatePair] from storage, replaces [RatePair.toRate] with the
 * provided [Rate], and persists the updated pair. The "from" rate and all other fields
 * are preserved unchanged.
 *
 * **Error handling:** Wrap the call site in try/catch. A database failure will
 * propagate as an exception and must be handled in the ViewModel to inform the user
 * that the selection was not saved.
 */
class SaveToRateUseCase(
    private val repository: RatePairStorageRepository,
) {
    suspend operator fun invoke(rate: Rate) {
        val current = repository.getRatePairAsFlow().first()
        repository.saveRatePair(current.copy(toRate = rate))
    }
}
