package io.github.viacheslav.chugunov.moniqo.core.usecase

import io.github.viacheslav.chugunov.moniqo.core.repository.SplashRepository
import kotlinx.coroutines.flow.Flow

/**
 * Returns whether all start-up initialization has completed as a continuous stream.
 *
 * Emits `false` on cold start and `true` once [SetSplashReadyUseCase] is invoked.
 */
class GetSplashReadyFlowUseCase(
    private val splashRepository: SplashRepository,
) {
    operator fun invoke(): Flow<Boolean> = splashRepository.isReady()
}
