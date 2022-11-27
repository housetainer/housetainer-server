package com.housetainer.adapter.web.handler

import com.housetainer.adapter.web.handler.HandlerExtension.accepted
import com.housetainer.adapter.web.handler.HandlerExtension.awaitBodyOrEmptyBodyException
import com.housetainer.common.log.logger
import com.housetainer.domain.model.invitation.RegisterInvitationRequest
import com.housetainer.domain.usecase.invitation.RegisterInvitationUseCase
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse

@Component
class InvitationHandler(
    private val registerInvitationUseCase: RegisterInvitationUseCase
) {

    private val log = logger()

    suspend fun registerInvitation(request: ServerRequest): ServerResponse {
        val invitationRequest: RegisterInvitationRequest = request.awaitBodyOrEmptyBodyException()
        log.info("register-invitation, request={}", invitationRequest)

        registerInvitationUseCase.registerInvitation(invitationRequest)

        return accepted()
    }
}
