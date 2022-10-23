package com.housetainer.domain.usecase.user

import com.housetainer.domain.entity.user.User
import com.housetainer.domain.model.user.CreateUserRequest

interface CreateUserUseCase {

    suspend fun createUser(createUserRequest: CreateUserRequest): User
}
