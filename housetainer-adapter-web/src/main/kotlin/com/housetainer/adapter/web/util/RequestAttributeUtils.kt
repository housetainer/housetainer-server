package com.housetainer.adapter.web.util

import com.housetainer.common.utils.Constants
import org.springframework.web.reactive.function.server.ServerRequest

object RequestAttributeUtils {

    fun ServerRequest.getAuthorizationToken(): String? = this.headers()
        .firstHeader(Constants.AUTHORIZATION)
        ?.takeIf { it.startsWith(Constants.BEARER_PREFIX) }
        ?.replaceFirst(Constants.BEARER_PREFIX, "")

    @Suppress("UNCHECKED_CAST")
    fun ServerRequest.loggingContext(): MutableMap<String, String?> {
        return this.attributes().getOrPut(Constants.LOGGING_CONTEXT) {
            mutableMapOf<String, String?>()
        } as MutableMap<String, String?>
    }
}
