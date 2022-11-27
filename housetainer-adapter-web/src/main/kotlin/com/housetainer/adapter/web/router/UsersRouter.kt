package com.housetainer.adapter.web.router

import com.housetainer.adapter.web.handler.UsersHandler
import com.housetainer.adapter.web.router.extension.RouterExtension.PATCH
import com.housetainer.adapter.web.router.filter.decorator.RequireUserTokenFilter
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.CoRouterFunctionDsl

@Configuration
class UsersRouter(private val usersHandler: UsersHandler) {

    fun route(): CoRouterFunctionDsl.() -> Unit = {
        PATCH("", usersHandler::updateUser, RequireUserTokenFilter())
    }
}
