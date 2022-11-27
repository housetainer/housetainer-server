package com.housetainer.module

import com.housetainer.application.user.UserService
import com.housetainer.domain.persistence.user.CreateUserCommand
import com.housetainer.domain.persistence.user.GetUserByIdQuery
import com.housetainer.domain.persistence.user.GetUserByNicknameQuery
import com.housetainer.domain.persistence.user.UpdateUserCommand
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class UserModule {

    @Bean
    fun userService(
        createUserCommand: CreateUserCommand,
        getUserByIdQuery: GetUserByIdQuery,
        getUserByNicknameQuery: GetUserByNicknameQuery,
        updateUserCommand: UpdateUserCommand
    ) = UserService(
        createUserCommand,
        getUserByIdQuery,
        getUserByNicknameQuery,
        updateUserCommand
    )
}
