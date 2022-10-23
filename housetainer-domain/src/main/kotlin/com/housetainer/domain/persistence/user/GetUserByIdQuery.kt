package com.housetainer.domain.persistence.user

import com.housetainer.domain.entity.user.User

interface GetUserByIdQuery {
    suspend fun getUserById(userId: String): User?
}
