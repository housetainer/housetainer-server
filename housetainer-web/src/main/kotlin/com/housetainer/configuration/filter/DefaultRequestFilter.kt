package com.housetainer.configuration.filter

import com.housetainer.common.utils.CommonUtils
import com.housetainer.common.utils.Constants
import com.housetainer.common.utils.CurrentContextInfo
import com.housetainer.common.utils.CurrentContextInfoUtils
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.util.StopWatch
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import reactor.util.context.ContextView

@Component
class DefaultRequestFilter : WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val request: ServerHttpRequest = exchange.request

        if ("/health" == request.uri.path) {
            return chain.filter(exchange)
                .doOnSuccess {
                    exchange.attributes[RouterFunctions.MATCHING_PATTERN_ATTRIBUTE] =
                        exchange.attributes[HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE]
                }
        }

        exchange.response.headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

        val traceId = buildTraceId(request)
        exchange.attributes[CurrentContextInfo.LOGGING_CONTEXT_TRACE_ID] = traceId

        val headers = mutableMapOf(CurrentContextInfo.LOGGING_CONTEXT_TRACE_ID to traceId)

        val defaultContextPair = CurrentContextInfoUtils.emptyContextMapPair(headers)

        val stopWatch = StopWatch()
        exchange.attributes["stopWatch"] = stopWatch.apply { start() }

        return chain
            .filter(exchange)
            .then(emptyDeferContextual { RequestLogger.logging(exchange) })
            .contextWrite {
                CurrentContextInfoUtils.clear()
                CurrentContextInfoUtils.traceId = traceId
                it.putAllMap(mutableMapOf(defaultContextPair))
            }
            .doFinally {
                if (stopWatch.isRunning) {
                    stopWatch.stop()
                }
            }
    }

    fun buildTraceId(request: ServerHttpRequest): String {
        return request.headers.getFirst(Constants.API_HEADER_TRACE_ID)
            ?.takeIf { it.isNotBlank() }
            ?: CommonUtils.randomUuid
    }

    private fun emptyDeferContextual(block: (ContextView) -> Unit): Mono<Void> = Mono.deferContextual {
        block(it)
        Mono.empty()
    }
}
