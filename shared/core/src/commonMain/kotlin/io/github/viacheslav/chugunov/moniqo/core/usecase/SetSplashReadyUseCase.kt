package io.github.viacheslav.chugunov.moniqo.core.usecase

import io.github.viacheslav.chugunov.moniqo.core.repository.SplashRepository

/**
 * Signals that all start-up initialization is complete.
 *
 * After this call, [GetSplashReadyFlowUseCase] will emit `true` and the splash screen
 * will be dismissed.
 */
class SetSplashReadyUseCase(
    private val splashRepository: SplashRepository,
) {
    operator fun invoke() = splashRepository.setReady()
}
