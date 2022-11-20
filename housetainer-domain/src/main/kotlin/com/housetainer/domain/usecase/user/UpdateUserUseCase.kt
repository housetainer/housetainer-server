package com.housetainer.domain.usecase.user

import com.housetainer.domain.model.user.UpdateUserRequest
import com.housetainer.domain.model.user.UserResponse

interface UpdateUserUseCase {

    suspend fun updateUser(updateUserRequest: UpdateUserRequest): UserResponse
}
