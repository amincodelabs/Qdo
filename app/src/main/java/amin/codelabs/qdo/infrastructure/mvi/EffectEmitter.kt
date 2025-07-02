package amin.codelabs.qdo.infrastructure.mvi

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Emits one-off UI effects (e.g., navigation, snackbars) for MVI.
 *
 * @param E Type of [UiEffect]
 * @param scope CoroutineScope to launch effect emissions.
 */
class EffectEmitter<E : UiEffect>(private val scope: CoroutineScope) {
    private val _effect = Channel<E>(Channel.BUFFERED)
    val effect: Flow<E> = _effect.receiveAsFlow()

    /**
     * Emit a one-off effect to the effect channel.
     */
    fun sendEffect(builder: () -> E) {
        scope.launch { _effect.send(builder()) }
    }
} 