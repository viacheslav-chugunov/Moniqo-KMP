package io.github.viacheslav.chugunov.moniqo.core.usecase

import io.github.viacheslav.chugunov.moniqo.core.model.RatePair
import io.github.viacheslav.chugunov.moniqo.core.repository.RatePairStorageRepository
import kotlinx.coroutines.flow.Flow

/**
 * Observes the currently selected rate pair as a continuous stream.
 *
 * The flow is backed by the local database and emits a new value whenever the
 * selected pair changes (e.g. after [SaveFromRateUseCase] or [SaveToRateUseCase]
 * is invoked). If no pair has been saved yet, the stream immediately emits the
 * default EUR/USD pair from the fallback dataset.
 *
 * **Error handling:** No wrapping required. The flow never errors — the fallback
 * ensures a value is always available.
 */
class GetRatePairFlowUseCase(
    private val repository: RatePairStorageRepository,
) {
    operator fun invoke(): Flow<RatePair> = repository.getRatePairAsFlow()
}
