package com.housetainer.domain.persistence.user

import com.housetainer.domain.entity.user.User
import com.housetainer.domain.model.user.CreateUserRequest

interface CreateUserCommand {
    suspend fun createUser(createUserRequest: CreateUserRequest): User
}
