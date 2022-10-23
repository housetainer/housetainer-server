package com.housetainer.common

import kotlinx.coroutines.runBlocking

object CoroutineTestUtils {
    @JvmStatic
    fun <T> executeSuspendFun(suspendMethod: suspend () -> T): T = runBlocking {
        suspendMethod()
    }
}
