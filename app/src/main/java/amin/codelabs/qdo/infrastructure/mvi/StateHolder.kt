package amin.codelabs.qdo.infrastructure.mvi

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Holds and manages the UI state for MVI.
 *
 * @param S Type of [UiState]
 * @param initialState The initial state value.
 */
class StateHolder<S : UiState>(initialState: S) {
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    /**
     * Update the current state using a reducer function.
     * The reducer receives the current state and returns a new state.
     */
    fun setState(reducer: S.() -> S) {
        _state.value = _state.value.reducer()
    }
} 