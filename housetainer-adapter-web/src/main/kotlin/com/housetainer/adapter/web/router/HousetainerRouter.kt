package com.housetainer.adapter.web.router

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.coRouter

@Component
class HousetainerRouter {

    @Bean
    fun housetainerRoutes(
        signRouter: SignRouter,
        usersRouter: UsersRouter
    ): RouterFunction<ServerResponse> = coRouter {
        "/sign".nest(signRouter.route())
        "/users".nest(usersRouter.route())
    }
}
