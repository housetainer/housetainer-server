package com.housetainer.application.user

import com.housetainer.common.log.logger
import com.housetainer.domain.entity.user.User
import com.housetainer.domain.model.user.CreateUserRequest
import com.housetainer.domain.persistence.user.CreateUserCommand
import com.housetainer.domain.usecase.user.CreateUserUseCase

class UserService(
    private val createUserCommand: CreateUserCommand
) : CreateUserUseCase {

    private val log = logger()

    override suspend fun createUser(createUserRequest: CreateUserRequest): User {
        log.info("create-user-start")
        return createUserCommand.createUser(createUserRequest)
            .apply {
                log.info("create-user-successfully, userId={}, email={}", userId, email)
            }
    }
}
