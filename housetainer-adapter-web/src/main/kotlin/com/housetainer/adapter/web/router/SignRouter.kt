package com.housetainer.adapter.web.router

import com.housetainer.adapter.web.handler.sign.SignHandler
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.CoRouterFunctionDsl

@Component
class SignRouter(
    private val signHandler: SignHandler
) {

    fun route(): CoRouterFunctionDsl.() -> Unit = {
        POST("up", signHandler::process)
    }
}
