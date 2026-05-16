package io.github.viacheslav.chugunov.moniqo.core.usecase

import io.github.viacheslav.chugunov.moniqo.core.repository.SplashRepository
import kotlinx.coroutines.flow.Flow

class GetSplashReadyFlowUseCase(
    private val splashRepository: SplashRepository,
) {
    operator fun invoke(): Flow<Boolean> = splashRepository.isReady()
}
