package com.example.ui.util.screenmodel


private val defaultKey = Any()


interface UiLoading {
    val isLoading: Boolean

    fun isLoading(key: Any?): Boolean
    fun startLoading(key: Any? = null): UiLoading
    fun stopLoading(key: Any? = null): UiLoading
}

private data class UiLoadingState(
    private val loadingState: Map<Any, Int> = HashMap(),
) : UiLoading {
    override val isLoading get() = loadingState.values.any { it > 0 }

    override fun isLoading(key: Any?): Boolean = (loadingState[validateKey(key)] ?: 0) > 0
    override fun startLoading(key: Any?): UiLoadingState {
        val k = validateKey(key)
        return copy(
            loadingState = HashMap(loadingState).apply { this[k] = (loadingState[k] ?: 0) + 1 }
        )
    }

    override fun stopLoading(key: Any?): UiLoadingState {
        val k = validateKey(key)
        return when(loadingState[k]) {
            null -> this
            in Int.MIN_VALUE..1 -> copy(loadingState = HashMap(loadingState).apply { this.remove(k)})
            else -> copy(loadingState = HashMap(loadingState).apply { this[k] = this[k]!! - 1 })
        }
    }

    private fun validateKey(key: Any?): Any = key ?: defaultKey
}

fun uiLoadingState(): UiLoading = UiLoadingState()