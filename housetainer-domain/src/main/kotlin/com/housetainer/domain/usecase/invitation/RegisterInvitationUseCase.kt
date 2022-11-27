package com.housetainer.domain.usecase.invitation

import com.housetainer.domain.model.invitation.RegisterInvitationRequest

interface RegisterInvitationUseCase {

    suspend fun registerInvitation(request: RegisterInvitationRequest)
}
