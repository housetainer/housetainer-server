package com.housetainer.domain.usecase.invitation

interface RegisterInvitationUseCase {

    suspend fun approveInvitation(code: String)
}
