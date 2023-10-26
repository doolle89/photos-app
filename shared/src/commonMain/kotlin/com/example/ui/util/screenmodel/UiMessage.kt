package com.example.ui.util.screenmodel

sealed interface UiMessage {
    val text: String
    val tag: Any?
    val action: Action?
}

data class InfoUiMessage(
    override val text: String,
    override val tag: Any? = null,
    override val action: Action? = null
) : UiMessage

data class ErrorUiMessage(
    override val text: String,
    override val tag: Any? = null,
    override val action: Action? = null
) : UiMessage

data class Action(
    val text: String,
    val block: () -> Unit
)
