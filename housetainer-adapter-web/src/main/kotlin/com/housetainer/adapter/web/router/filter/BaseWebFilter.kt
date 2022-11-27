package com.housetainer.adapter.web.router.filter

import org.springframework.web.reactive.function.server.ServerRequest

interface BaseWebFilter {
    suspend fun test(request: ServerRequest)
}
