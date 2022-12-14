package com.housetainer.adapter.persistence.repository.r2dbc

import com.housetainer.adapter.persistence.repository.entity.UserEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserR2DBCRepository : CoroutineCrudRepository<UserEntity, Long> {
    suspend fun findByUserId(userId: String): UserEntity?

    suspend fun findByEmail(email: String): UserEntity?

    suspend fun findByNickname(nickname: String): UserEntity?
}
