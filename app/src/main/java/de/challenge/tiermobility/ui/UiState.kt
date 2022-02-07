package de.challenge.tiermobility.ui

sealed class UiState<out T> {
    class Loading <out T> : UiState<T>()
    data class Data <out T>(val data : T)  : UiState<T>()
    class NoLocationPermission <out T> : UiState<T>()
    class NoInternet<out T> : UiState<T>()
    class ServerError<out T> : UiState<T>()
}

