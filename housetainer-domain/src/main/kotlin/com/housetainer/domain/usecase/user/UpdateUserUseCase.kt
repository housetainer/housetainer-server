package com.housetainer.domain.usecase.user

import com.housetainer.domain.model.user.InternalUpdateUserRequest
import com.housetainer.domain.model.user.UserResponse

interface UpdateUserUseCase {

    suspend fun updateUser(updateUserRequest: InternalUpdateUserRequest): UserResponse
}
