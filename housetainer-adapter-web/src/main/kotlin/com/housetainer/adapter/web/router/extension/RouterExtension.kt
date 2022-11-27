package com.housetainer.adapter.web.router.extension

import com.housetainer.adapter.web.router.filter.BaseWebFilter
import org.springframework.web.reactive.function.server.CoRouterFunctionDsl
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse

object RouterExtension {
    @Suppress("FunctionNaming")
    fun CoRouterFunctionDsl.GET(
        pattern: String,
        handler: suspend (ServerRequest) -> ServerResponse,
        filter: BaseWebFilter
    ) = withFilter(::GET, pattern, handler, filter)

    @Suppress("FunctionNaming")
    fun CoRouterFunctionDsl.POST(
        pattern: String,
        handler: suspend (ServerRequest) -> ServerResponse,
        filter: BaseWebFilter
    ) = withFilter(::POST, pattern, handler, filter)

    @Suppress("FunctionNaming")
    fun CoRouterFunctionDsl.PATCH(
        pattern: String,
        handler: suspend (ServerRequest) -> ServerResponse,
        filter: BaseWebFilter
    ) = withFilter(::PATCH, pattern, handler, filter)

    private fun withFilter(
        function: (pattern: String, handler: suspend (ServerRequest) -> ServerResponse) -> Unit,
        pattern: String,
        handler: suspend (ServerRequest) -> ServerResponse,
        filter: BaseWebFilter
    ) = function(pattern) { request ->
        filter.test(request)
        handler.invoke(request)
    }
}
