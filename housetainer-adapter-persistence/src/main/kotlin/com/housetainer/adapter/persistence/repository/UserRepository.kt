package com.housetainer.adapter.persistence.repository

import com.housetainer.adapter.persistence.repository.entity.UserEntity
import com.housetainer.adapter.persistence.repository.r2dbc.UserR2DBCRepository
import com.housetainer.domain.entity.user.User
import com.housetainer.domain.model.user.CreateUserRequest
import com.housetainer.domain.persistence.user.CreateUserCommand
import com.housetainer.domain.persistence.user.GetUserByEmailQuery
import com.housetainer.domain.persistence.user.GetUserByIdQuery
import org.springframework.stereotype.Component

@Component
class UserRepository(
    val repository: UserR2DBCRepository
) : CreateUserCommand,
    GetUserByIdQuery,
    GetUserByEmailQuery {

    override suspend fun createUser(createUserRequest: CreateUserRequest): User {
        return repository.save(
            UserEntity(
                email = createUserRequest.email,
                authId = createUserRequest.authId,
                authProvider = createUserRequest.authProvider,
                name = createUserRequest.name,
                nickname = createUserRequest.nickname,
                gender = createUserRequest.gender,
                birthday = createUserRequest.birthday,
                phoneNumber = createUserRequest.phoneNumber,
                profileImage = createUserRequest.profileImage,
                countryCode = createUserRequest.countryCode,
                languageCode = createUserRequest.languageCode,
                type = createUserRequest.type,
                status = createUserRequest.status,
                createTime = createUserRequest.createTime,
                updateTime = createUserRequest.updateTime
            )
        ).toUser()
    }

    override suspend fun getUserById(userId: String): User? {
        return repository.findByUserId(userId)?.toUser()
    }

    override suspend fun getUserByEmail(email: String): User? {
        return repository.findByEmail(email)?.toUser()
    }

    private fun UserEntity.toUser(): User = User(
        userId,
        email,
        authId,
        authProvider,
        name,
        nickname,
        gender,
        birthday,
        phoneNumber,
        profileImage,
        countryCode,
        languageCode,
        type,
        status,
        createTime,
        updateTime,
    )
}
