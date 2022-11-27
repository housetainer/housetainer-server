package com.housetainer.adapter.web.router.filter.decorator

import com.housetainer.adapter.web.router.filter.BaseWebFilter
import com.housetainer.common.utils.Constants
import com.housetainer.domain.entity.exception.BaseException
import com.housetainer.domain.port.token.TokenService
import org.springframework.web.reactive.function.server.ServerRequest

class RequireUserTokenFilter(
    next: BaseWebFilter = BaseFilter()
) : FilterDecorator(next) {
    override suspend fun test(request: ServerRequest) {
        val headerToken: String = request.headers().firstHeader(Constants.AUTHORIZATION)
            ?.takeIf { it.isNotBlank() && it.startsWith(Constants.BEARER_PREFIX) }
            ?.replace(Constants.BEARER_PREFIX, "")
            ?: throw tokenRequiredException()

        val tokenInformation = TokenService.validateToken(headerToken)
        request.attributes()[Constants.HOUSETAINER_TOKEN_HEADER] = headerToken
        request.attributes()[HEADER_TOKEN_INFORMATION] = tokenInformation

        super.test(request)
    }

    companion object {
        const val HEADER_TOKEN_INFORMATION = "X-HOUSETAINER-TOKEN-INFO"

        @JvmStatic
        fun tokenRequiredException() = BaseException(401, "token required")
    }
}
