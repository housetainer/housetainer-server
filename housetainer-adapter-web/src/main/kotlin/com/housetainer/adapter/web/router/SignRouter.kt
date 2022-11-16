package com.housetainer.adapter.web.router

import com.housetainer.adapter.web.handler.SignHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class SignRouter {

    @Bean
    fun route(signHandler: SignHandler) = coRouter {
        "/sign".nest {
            POST("/up", signHandler::signUp)
            POST("/in", signHandler::signIn)
            POST("/token/renew", signHandler::renewToken)
        }
    }
}
