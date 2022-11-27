package com.housetainer.module

import com.housetainer.application.invitation.InvitationService
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class InvitationModule {

    @Bean
    fun invitationService() = InvitationService()
}
