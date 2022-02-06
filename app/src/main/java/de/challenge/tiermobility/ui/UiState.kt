package de.challenge.tiermobility.ui

sealed class UiState<out T> {
    class Loading <out T> : UiState<T>()
    class Data <out T>(val data : T)  : UiState<T>()
    class Error <out T> (val viewError: ViewError) : UiState<T>()
}
