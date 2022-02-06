package de.challenge.tiermobility.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface CoroutineContextProvider {
    fun mainDispatcher(): CoroutineDispatcher
    fun defaultDispatcher(): CoroutineDispatcher
    fun ioDispatcher(): CoroutineDispatcher
    fun unconfinedDispatcher(): CoroutineDispatcher
}

class DefaultCoroutineContextProvider : CoroutineContextProvider {
    override fun mainDispatcher() = Dispatchers.Main
    override fun defaultDispatcher() = Dispatchers.Default
    override fun ioDispatcher() = Dispatchers.IO
    override fun unconfinedDispatcher() = Dispatchers.Unconfined
}

class TestCoroutineContextProvider : CoroutineContextProvider {
    override fun mainDispatcher(): CoroutineDispatcher = Dispatchers.Unconfined
    override fun defaultDispatcher(): CoroutineDispatcher = Dispatchers.Unconfined
    override fun ioDispatcher(): CoroutineDispatcher = Dispatchers.Unconfined
    override fun unconfinedDispatcher(): CoroutineDispatcher = Dispatchers.Unconfined
}
