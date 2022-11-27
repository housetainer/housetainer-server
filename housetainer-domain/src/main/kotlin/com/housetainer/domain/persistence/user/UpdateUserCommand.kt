package com.housetainer.domain.persistence.user

import com.housetainer.domain.entity.user.User
import com.housetainer.domain.model.user.InternalUpdateUserRequest

interface UpdateUserCommand {
    suspend fun updateUser(updateUserRequest: InternalUpdateUserRequest): User
}
