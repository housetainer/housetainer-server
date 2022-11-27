package com.housetainer.adapter.web.router.filter.decorator

import com.housetainer.adapter.web.router.filter.BaseWebFilter
import org.springframework.web.reactive.function.server.ServerRequest

internal class BaseFilter : BaseWebFilter {
    override suspend fun test(request: ServerRequest) = Unit
}
