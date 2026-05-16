package io.github.viacheslav.chugunov.moniqo.core.usecase

import io.github.viacheslav.chugunov.moniqo.core.repository.SplashRepository

class SetSplashReadyUseCase(
    private val splashRepository: SplashRepository,
) {
    operator fun invoke() = splashRepository.setReady()
}
