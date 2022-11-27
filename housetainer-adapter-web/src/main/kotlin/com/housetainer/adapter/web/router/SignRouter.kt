package com.housetainer.adapter.web.router

import com.housetainer.adapter.web.handler.SignHandler
import com.housetainer.adapter.web.router.extension.RouterExtension.POST
import com.housetainer.adapter.web.router.filter.decorator.RequireUserTokenFilter
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.CoRouterFunctionDsl

@Configuration
class SignRouter(private val signHandler: SignHandler) {

    fun route(): CoRouterFunctionDsl.() -> Unit = {
        POST("/up", signHandler::signUp)
        POST("/in", signHandler::signIn, RequireUserTokenFilter())
        POST("/token/renew", signHandler::renewToken)
    }
}
