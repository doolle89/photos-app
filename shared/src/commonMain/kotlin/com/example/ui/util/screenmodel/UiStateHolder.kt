package com.example.ui.util.screenmodel

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface UiStateHolder<State> {
    val uiState: StateFlow<UiState<State>>

    fun setState(state: State)
    fun resetState(clearStateUpdates: Boolean = true)
    fun updateState(state: (State) -> State)
    fun <T> updateState(flow: Flow<T>, transform: (State, T) -> State)
    fun startLoading(key: Any? = null)
    fun stopLoading(key: Any? = null)
    fun addMessage(uiMessage: UiMessage)
    fun removeMessage(uiMessage: UiMessage)
}