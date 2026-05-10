package io.github.viacheslav.chugunov.moniqo.ui.core

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

abstract class AppViewModel<S, I, E>(
    initialState: S,
) : ViewModel() {
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    private val _effects = Channel<E>(Channel.BUFFERED)
    val effects: Flow<E> = _effects.receiveAsFlow()

    abstract fun onIntent(intent: I)

    protected fun updateState(block: (S) -> S) {
        _state.update(block)
    }

    protected suspend fun sendEffect(effect: E) {
        _effects.send(effect)
    }
}
