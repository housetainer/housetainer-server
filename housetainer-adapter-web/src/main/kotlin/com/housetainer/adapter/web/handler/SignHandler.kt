package com.housetainer.adapter.web.handler

import com.housetainer.adapter.web.handler.HandlerExtension.awaitBodyOrEmptyBodyException
import com.housetainer.adapter.web.handler.HandlerExtension.noContent
import com.housetainer.adapter.web.handler.HandlerExtension.ok
import com.housetainer.adapter.web.util.RequestAttributeUtils.getAuthorizationToken
import com.housetainer.common.log.logger
import com.housetainer.common.utils.Constants
import com.housetainer.domain.entity.exception.BaseException
import com.housetainer.domain.model.auth.RenewTokenRequest
import com.housetainer.domain.model.auth.SignUpRequest
import com.housetainer.domain.usecase.auth.SignUseCase
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse

@Component
class SignHandler(
    private val signUseCase: SignUseCase
) {

    private val log = logger()

    suspend fun signUp(request: ServerRequest): ServerResponse {
        val signUpRequest: SignUpRequest = request.awaitBodyOrEmptyBodyException()
        log.debug("sign-up, request={}", signUpRequest)
        val result = signUseCase.signUp(signUpRequest)

        return ok(result.user) {
            it.add(Constants.HOUSETAINER_TOKEN_HEADER, result.token)
        }
    }

    suspend fun signIn(request: ServerRequest): ServerResponse {
        val token = request.getAuthorizationToken() ?: throw BaseException(401, "token is empty")
        val result = signUseCase.signIn(token)
        return ok(result)
    }

    suspend fun renewToken(request: ServerRequest): ServerResponse {
        val renewTokenRequest: RenewTokenRequest = request.awaitBodyOrEmptyBodyException()
        log.debug("renew-token, request={}", renewTokenRequest)
        val token = signUseCase.renewToken(renewTokenRequest)

        return noContent() {
            it.add(Constants.HOUSETAINER_TOKEN_HEADER, token)
        }
    }
}
