package com.housetainer.domain.usecase.user

import com.housetainer.domain.entity.user.User
import com.housetainer.domain.model.user.UpdateUserRequest

interface UpdateUserUseCase {

    suspend fun updateUser(updateUserRequest: UpdateUserRequest): User
}
