package com.example.ui.util.screenmodel

interface UiState<State> {
    val state: State
    val messages: List<UiMessage>
    val isLoading: Boolean

    fun isLoading(key: Any?): Boolean
    fun startLoading(key: Any? = null): UiState<State>
    fun stopLoading(key: Any? = null): UiState<State>
    fun addMessage(uiMessage: UiMessage): UiState<State>
    fun removeMessage(uiMessage: UiMessage): UiState<State>
}