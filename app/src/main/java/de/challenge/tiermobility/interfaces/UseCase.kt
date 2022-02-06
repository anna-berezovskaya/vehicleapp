package de.challenge.tiermobility.interfaces

interface UseCase <T> {
    suspend fun run() : T
}