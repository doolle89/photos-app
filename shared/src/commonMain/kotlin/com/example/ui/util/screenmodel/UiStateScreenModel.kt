package com.example.ui.util.screenmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


// State
private data class UiStateImpl<State> (
    override val state: State,
    private val loadingState: UiLoading = uiLoadingState(),
    override val messages: List<UiMessage> = emptyList()
) : UiState<State> {
    override val isLoading: Boolean get () = loadingState.isLoading

    override fun isLoading(key: Any?): Boolean = loadingState.isLoading(key)
    override fun startLoading(key: Any?): UiStateImpl<State> = copy(loadingState = loadingState.startLoading(key))
    override fun stopLoading(key: Any?): UiStateImpl<State> = if (isLoading) copy(loadingState = loadingState.stopLoading(key)) else this
    override fun addMessage(uiMessage: UiMessage): UiStateImpl<State> = copy(messages = messages + uiMessage)
    override fun removeMessage(uiMessage: UiMessage): UiStateImpl<State> = copy(messages = messages.filterNot { it == uiMessage })
}

private fun <State> uiState(
    state: State
): UiStateImpl<State> = UiStateImpl(state)


// StateHolder

private fun <State> uiStateHolder(
    initialState: State,
    coroutineScope: () -> CoroutineScope
) = object : UiStateHolder<State> {
    private val internalUiState = MutableStateFlow(uiState<State>(initialState))
    private var _uiState: StateFlow<UiState<State>> = internalUiState
    override val uiState: StateFlow<UiState<State>> get() = _uiState

    override fun setState(state: State) {
        internalUiState.update {
            it.copy(state = state)
        }
    }

    override fun resetState(clearStateUpdates: Boolean) {
        coroutineScope().coroutineContext.cancelChildren()
        internalUiState.value = uiState(initialState)
        if (clearStateUpdates) {
            _uiState = internalUiState
        }
    }

    override fun updateState(state: (State) -> State) {
        internalUiState.update {
            it.copy(state = state(it.state))
        }
    }

    override fun <T> updateState(
        flow: Flow<T>,
        transform: (State, T) -> State
    ) {
        _uiState = uiState.let { previousUiState ->
            channelFlow {
                launch {
                    flow.collect { value ->
                        updateState { transform(it, value) }
                    }
                }
                previousUiState.collect(::send)
            }.stateIn(coroutineScope(), SharingStarted.WhileSubscribed(5000), previousUiState.value)
        }
    }

    override fun startLoading(key: Any?) {
        internalUiState.update {
            it.startLoading(key)
        }
    }

    override fun stopLoading(key: Any?) {
        internalUiState.update {
            it.stopLoading(key)
        }
    }

    override fun addMessage(uiMessage: UiMessage) {
        internalUiState.update {
            it.addMessage(uiMessage)
        }
    }

    override fun removeMessage(uiMessage: UiMessage) {
        internalUiState.update {
            it.removeMessage(uiMessage)
        }
    }
}


// ViewModel

// currently it is not possible to have delegation with "by" which supports final methods
// e.g. abstract class UiStateViewModel<T>(initialState: T) : ViewModel(), UiStateHolder<T> by uiStateHolder(initialState)
abstract class UiStateScreenModel<State>(initialState: State) : ScreenModel, UiStateHolder<State> {
    private val delegate = uiStateHolder(initialState, ::screenModelScope)
    final override val uiState: StateFlow<UiState<State>> get() = delegate.uiState

    final override fun setState(state: State) = delegate.setState(state)
    final override fun resetState(clearStateUpdates: Boolean) = delegate.resetState(clearStateUpdates)
    final override fun updateState(state: (State) -> State) = delegate.updateState(state)
    final override fun <T> updateState(flow: Flow<T>, transform: (State, T) -> State) = delegate.updateState(flow, transform)
    final override fun startLoading(key: Any?) = delegate.startLoading(key)
    final override fun stopLoading(key: Any?) = delegate.stopLoading(key)
    final override fun addMessage(uiMessage: UiMessage) = delegate.addMessage(uiMessage)
    final override fun removeMessage(uiMessage: UiMessage) = delegate.removeMessage(uiMessage)
}


// Extensions

val <State, StateHolder : UiStateHolder<State>> StateHolder.state
    get() = uiState.value.state

fun <State, UiStateViewModel> UiStateViewModel.launchWithLoading(
    key: Any? = null,
    block: suspend () -> Unit
) where UiStateViewModel : ScreenModel, UiStateViewModel : UiStateHolder<State> {
    startLoading(key)
    screenModelScope.launch {
        block()
        stopLoading(key)
    }
}

private fun <T, State, UiStateViewModel> UiStateViewModel.collectCompletableWithLoading(
    flow: Flow<T>,
    startLoadingOnlyIfNotStarted: Boolean = true,
    loadingKey: Any? = null,
    collector: FlowCollector<T>
) where UiStateViewModel : ScreenModel, UiStateViewModel : UiStateHolder<State> {
    if (startLoadingOnlyIfNotStarted && !uiState.value.isLoading(loadingKey)) {
        startLoading(loadingKey)
    } else if (!startLoadingOnlyIfNotStarted) {
        startLoading(loadingKey)
    }
    screenModelScope.launch {
        flow.onCompletion { stopLoading(loadingKey) }.collect(collector)
    }
}

fun <T, State, UiStateViewModel> UiStateViewModel.collectFirstWithLoading(
    flow: Flow<T>,
    startLoadingOnlyIfNotStarted: Boolean = true,
    loadingKey: Any? = null,
    collector: FlowCollector<T>
) where UiStateViewModel : ScreenModel, UiStateViewModel : UiStateHolder<State> = collectCompletableWithLoading(
    flow = flow.take(1),
    startLoadingOnlyIfNotStarted = startLoadingOnlyIfNotStarted,
    loadingKey = loadingKey,
    collector = collector
)

fun <T, State, UiStateViewModel> UiStateViewModel.updateWithLoading(
    flow: Flow<T>,
    startLoadingOnlyIfNotStarted: Boolean = true,
    loadingKey: Any? = null,
    transform: (State, T) -> State
) where UiStateViewModel : ScreenModel, UiStateViewModel : UiStateHolder<State> = collectFirstWithLoading(
    flow = flow,
    startLoadingOnlyIfNotStarted = startLoadingOnlyIfNotStarted,
    loadingKey = loadingKey,
    collector = { value ->
        updateState { transform(it, value) }
        updateState(flow, transform)
    }
)