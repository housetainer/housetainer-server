package com.housetainer.adapter.web.router

import com.housetainer.adapter.web.handler.InvitationHandler
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.CoRouterFunctionDsl

@Configuration
class InvitationRouter(private val invitationHandler: InvitationHandler) {

    fun route(): CoRouterFunctionDsl.() -> Unit = {
        POST("register", invitationHandler::registerInvitation)
    }
}
