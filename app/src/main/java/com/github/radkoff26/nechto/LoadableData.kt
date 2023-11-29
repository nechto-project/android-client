package com.github.radkoff26.nechto

sealed class LoadableData<T> {

    class Loading<T> : LoadableData<T>()

    data class Loaded<T>(val data: T) : LoadableData<T>()

    data class Failed<T>(val code: Int, val message: String) : LoadableData<T>()
}
