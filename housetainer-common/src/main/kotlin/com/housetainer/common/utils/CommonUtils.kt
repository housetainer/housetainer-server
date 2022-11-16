package com.housetainer.common.utils

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.util.TimeZone
import java.util.UUID

object CommonUtils {

    @JvmStatic
    var mapper: ObjectMapper = jacksonObjectMapper()
        .apply(commonMapperConfig())
        .apply {
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
        }

    private fun commonMapperConfig(): ObjectMapper.() -> Unit = {
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        setTimeZone(TimeZone.getTimeZone("UTC"))
        configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        registerModule(JavaTimeModule())
    }

    val randomUuid: String
        get() = UUID.randomUUID().toString()

    fun Throwable.extractCausedBy(len: Int = 4): String {
        val cause = this.cause ?: this
        val stacktraces = cause.stackTrace.toList().subList(0, Integer.min(len, cause.stackTrace.size))
        return "$cause\n\t${stacktraces.joinToString("\n\t")}\n\t..."
    }

    fun bearerToken(token: String) = "${Constants.BEARER_PREFIX}$token"

    inline fun <T> T.applyWhen(predicate: Boolean, block: T.() -> Unit): T {
        if (predicate) {
            this.apply(block)
        }
        return this
    }

    inline fun <T> T.applyIf(predicate: T.() -> Boolean, block: T.() -> Unit): T {
        if (predicate.invoke(this)) {
            this.apply(block)
        }
        return this
    }

    fun Int.is2xxSuccessful(): Boolean = this in 200..299

    fun Int.is5xxServerError(): Boolean = this in 500..599
}
