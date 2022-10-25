package com.housetainer.module

import com.housetainer.application.user.UserService
import com.housetainer.domain.persistence.user.CreateUserCommand
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class UserModule {

    @Bean
    fun userService(
        createUserCommand: CreateUserCommand
    ) = UserService(
        createUserCommand
    )
}
