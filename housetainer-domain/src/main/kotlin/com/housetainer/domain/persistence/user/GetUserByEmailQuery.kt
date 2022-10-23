package com.housetainer.domain.persistence.user

import com.housetainer.domain.entity.user.User

interface GetUserByEmailQuery {
    suspend fun getUserByEmail(email: String): User?
}
