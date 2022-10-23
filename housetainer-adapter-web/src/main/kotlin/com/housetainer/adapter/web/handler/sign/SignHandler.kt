package com.housetainer.adapter.web.handler.sign

import com.housetainer.common.log.logger
import com.housetainer.domain.model.auth.SignUpRequest
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.noContent
import org.springframework.web.reactive.function.server.awaitBodyOrNull
import org.springframework.web.reactive.function.server.buildAndAwait

@Component
class SignHandler {

    private val log = logger()

    suspend fun process(request: ServerRequest): ServerResponse {
        val request = request.awaitBodyOrNull<SignUpRequest>()
        log.info("sign-up, request={}", request)
        return noContent().buildAndAwait()
    }
}
