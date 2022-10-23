package com.housetainer.common.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.slf4j.MDCContext
import kotlinx.coroutines.withContext

object CoroutineUtils {

    @JvmStatic
    var isTesting: Boolean = false

    suspend fun <T> wrap(block: suspend () -> T): T {
        return if (isTesting) {
            runBlocking { block() }
        } else {
            block()
        }
    }

    suspend fun <T> withCoroutineScope(block: suspend CoroutineScope.() -> T): T {
        return if (isTesting) {
            runBlocking { block() }
        } else {
            withContext(Dispatchers.IO + MDCContext()) {
                block()
            }
        }
    }

    fun runAsync(action: suspend () -> Unit) {
        if (isTesting) {
            runBlocking { action() }
        } else {
            CoroutineScope(MDCContext()).launch {
                action()
            }
        }
    }

    suspend fun <T> blockToAsync(block: () -> T): T {
        return if (isTesting) {
            block()
        } else {
            withContext(Dispatchers.IO + MDCContext()) {
                block()
            }
        }
    }
}
