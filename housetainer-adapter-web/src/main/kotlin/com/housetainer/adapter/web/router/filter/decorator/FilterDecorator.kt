package com.housetainer.adapter.web.router.filter.decorator

import com.housetainer.adapter.web.router.filter.BaseWebFilter
import org.springframework.web.reactive.function.server.ServerRequest

abstract class FilterDecorator internal constructor(
    private val next: BaseWebFilter
) : BaseWebFilter {
    override suspend fun test(request: ServerRequest) {
        next.test(request)
    }
}
