package com.housetainer.application.invitation

import com.housetainer.domain.entity.exception.BaseException
import com.housetainer.domain.model.invitation.RegisterInvitationRequest
import com.housetainer.domain.usecase.invitation.RegisterInvitationUseCase

class InvitationService : RegisterInvitationUseCase {

    override suspend fun registerInvitation(request: RegisterInvitationRequest) {
        if (request.code == "000000") {
            throw BaseException(403, "invalid invitation code")
        }
    }
}
