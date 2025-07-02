package amin.codelabs.qdo.infrastructure.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

/**
 * Processes user intents sequentially for MVI.
 *
 * @param I Type of [UiIntent]
 * @param scope CoroutineScope to launch intent processing.
 * @param handler Suspend function to handle each intent.
 */
class IntentProcessor<I : UiIntent>(
    private val scope: CoroutineScope,
    private val handler: suspend (I) -> Unit
) {
    private val intentChannel = Channel<I>(Channel.UNLIMITED)

    init {
        scope.launch {
            intentChannel.consumeAsFlow().collect { handler(it) }
        }
    }

    /**
     * Send a new intent to be processed.
     */
    fun processIntent(intent: I) {
        scope.launch { intentChannel.send(intent) }
    }
} 