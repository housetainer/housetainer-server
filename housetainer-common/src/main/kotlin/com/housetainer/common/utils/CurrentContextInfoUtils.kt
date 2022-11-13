package com.housetainer.common.utils

import com.housetainer.common.log.logger
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.reactor.ReactorContext
import org.slf4j.MDC
import reactor.util.context.Context
import java.util.concurrent.ConcurrentHashMap

object CurrentContextInfoUtils : CurrentContextInfo() {

    private const val CONTEXT_MAP = "reactor-context-map"

    fun emptyContextMapPair(defaultValues: Map<String, String?> = mapOf()): Pair<String, MutableMap<String, String?>> {
        return CONTEXT_MAP to ConcurrentHashMap(defaultValues)
    }

    suspend fun getContextMap(): MutableMap<String, String?>? =
        currentCoroutineContext()[ReactorContext]
            ?.context
            ?.get<MutableMap<String, String?>>(CONTEXT_MAP)

    suspend fun putToContext(key: String, value: String?) = getContextMap()?.put(key, value)

    suspend fun putToContexts(map: Map<String, String?>) = getContextMap()?.putAll(map)

    suspend inline fun getFromContext(key: String): String? = getContextMap()?.get(key)

    fun Context.copyContextMapToMdc() {
        if (!isEmpty) { // not Context0
            getOrEmpty<MutableMap<String, String?>>(CONTEXT_MAP)
                .ifPresent {
                    CurrentContextInfoUtils.setMdcAttributes(it)
                }
        }
    }
}

open class CurrentContextInfo {

    private val log = logger()

    /**
     * Warning:
     *
     * The value added using this method may not be delivered to downstream.
     *
     * If you want to deliver to downstream, you can use `CurrentContextInfoUtils.putToContexts`.
     */
    fun setMdcAttributes(m: Map<String, String?>) {
        try {
            MDC.setContextMap(m)
        } catch (ignore: Exception) {
            log.trace(IGNORED_EXCEPTION_MESSAGE)
        }
    }

    private fun setMdcAttribute(name: String, value: String?) {
        try {
            MDC.put(name, value)
        } catch (ignore: Exception) {
            log.trace(IGNORED_EXCEPTION_MESSAGE)
        }
    }

    fun getMdcAttribute(name: String): String? =
        try {
            MDC.get(name)
        } catch (ignore: Exception) {
            log.trace(IGNORED_EXCEPTION_MESSAGE)
            null
        }

    fun getAllMdcAttributes(): MutableMap<String, String> = MDC.getCopyOfContextMap() ?: ConcurrentHashMap()

    fun clear() {
        try {
            MDC.clear()
        } catch (ignore: Exception) {
            log.trace(IGNORED_EXCEPTION_MESSAGE)
        }
    }

    internal var isCoroutineTesting: Boolean
        get() {
            return getMdcAttribute(COROUTINE_TESTING)?.toBoolean() ?: false
        }
        set(value) {
            setMdcAttribute(COROUTINE_TESTING, value.toString())
        }

    var traceId: String?
        get() {
            return getMdcAttribute(LOGGING_CONTEXT_TRACE_ID)
        }
        set(value) {
            setMdcAttribute(LOGGING_CONTEXT_TRACE_ID, value)
        }

    companion object {
        const val LOGGING_CONTEXT_TRACE_ID = "traceId"
        const val COROUTINE_TESTING = "X-COROUTINE-ASYNC-TO-SYNC"
        const val IGNORED_EXCEPTION_MESSAGE = "no thread bound request context found"
    }
}
