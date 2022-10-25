package com.housetainer.adapter.web.handler

import com.housetainer.adapter.web.handler.HandlerExtension.awaitBodyOrEmptyBodyException
import com.housetainer.adapter.web.handler.HandlerExtension.ok
import com.housetainer.common.log.logger
import com.housetainer.domain.model.auth.SignInRequest
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
        return ok(result)
    }

    suspend fun signIn(request: ServerRequest): ServerResponse {
        val signInRequest: SignInRequest = request.awaitBodyOrEmptyBodyException()
        log.debug("sign-up, request={}", signInRequest)
        val result = signUseCase.signIn(signInRequest)
        return ok(result)
    }
}
