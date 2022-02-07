package de.challenge.tiermobility.utils


import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object CoroutineUtils {

    interface Callback<T> {
        fun onComplete(result: T)
        fun onException(e: Exception?)
    }


    suspend fun <T> awaitCallback(block: (Callback<T>) -> Unit): T =
        suspendCancellableCoroutine { cont ->
            block(object : Callback<T> {
                override fun onComplete(result: T) =
                    cont.resume(result)

                override fun onException(e: Exception?) {
                    e?.let { cont.resumeWithException(it) }
                }
            })
        }
}