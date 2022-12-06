package com.housetainer.adapter.web.handler

import com.housetainer.adapter.web.handler.HandlerExtension.accepted
import com.housetainer.adapter.web.handler.HandlerExtension.notEmptyPathVariable
import com.housetainer.common.log.logger
import com.housetainer.domain.entity.exception.BaseException
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
        val code = request.notEmptyPathVariable("code", BaseException(400, "code is required"))
        log.info("approve-invitation, code={}", code)

        registerInvitationUseCase.approveInvitation(code)

        return accepted()
    }
}
