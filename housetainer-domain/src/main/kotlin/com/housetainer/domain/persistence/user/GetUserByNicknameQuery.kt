package com.housetainer.domain.persistence.user

import com.housetainer.domain.entity.user.User

interface GetUserByNicknameQuery {
    suspend fun getUserByNickname(nickname: String): User?
}
