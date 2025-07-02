package amin.codelabs.qdo.infrastructure.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Base ViewModel for MVI architecture using composition.
 * Delegates state, effect, and intent handling to dedicated components.
 *
 * @param I Type of [UiIntent]
 * @param S Type of [UiState]
 * @param E Type of [UiEffect]
 *
 * @property initialState The initial UI state.
 */
abstract class BaseMviViewModel<I : UiIntent, S : UiState, E : UiEffect>(
    initialState: S
) : ViewModel() {
    /** Holds and manages the UI state. */
    private val stateHolder = StateHolder(initialState)
    val state: StateFlow<S> = stateHolder.state

    /** Emits one-off UI effects. */
    private val effectEmitter = EffectEmitter<E>(viewModelScope)
    val effect: Flow<E> = effectEmitter.effect

    /** Processes user intents sequentially. */
    private val intentProcessor = IntentProcessor<I>(viewModelScope) { handleIntent(it) }

    /**
     * Process a new intent from the UI.
     */
    fun processIntent(intent: I) = intentProcessor.processIntent(intent)

    /**
     * Update the current UI state using a reducer function.
     */
    protected fun setState(reducer: S.() -> S) = stateHolder.setState(reducer)

    /**
     * Emit a one-off effect (such as navigation or a snackbar).
     */
    protected fun sendEffect(builder: () -> E) = effectEmitter.sendEffect(builder)

    /**
     * Handle an intent. Must be implemented by subclasses.
     */
    protected abstract suspend fun handleIntent(intent: I)
} 