package com.housetainer.adapter.persistence.repository

import com.housetainer.adapter.persistence.repository.entity.UserEntity
import com.housetainer.adapter.persistence.repository.r2dbc.UserR2DBCRepository
import com.housetainer.domain.entity.exception.BaseException
import com.housetainer.domain.entity.user.User
import com.housetainer.domain.model.user.CreateUserRequest
import com.housetainer.domain.model.user.UpdateUserRequest
import com.housetainer.domain.persistence.user.CreateUserCommand
import com.housetainer.domain.persistence.user.GetUserByEmailQuery
import com.housetainer.domain.persistence.user.GetUserByIdQuery
import com.housetainer.domain.persistence.user.GetUserByNicknameQuery
import com.housetainer.domain.persistence.user.UpdateUserCommand
import org.springframework.stereotype.Component

@Component
class UserRepository(
    val repository: UserR2DBCRepository
) : CreateUserCommand,
    GetUserByIdQuery,
    GetUserByEmailQuery,
    GetUserByNicknameQuery,
    UpdateUserCommand {

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

    override suspend fun getUserByNickname(nickname: String): User? {
        return repository.findByNickname(nickname)?.toUser()
    }

    override suspend fun updateUser(updateUserRequest: UpdateUserRequest): User {
        val userEntity = repository.findByUserId(updateUserRequest.userId) ?: throw userNotFoundException()

        updateUserRequest.nickname?.let { userEntity.nickname = it }
        updateUserRequest.gender?.let { userEntity.gender = it }
        updateUserRequest.birthday?.let { userEntity.birthday = it }
        updateUserRequest.phoneNumber?.let { userEntity.phoneNumber = it }
        updateUserRequest.profileImage?.let { userEntity.profileImage = it }
        updateUserRequest.countryCode?.let { userEntity.countryCode = it }
        updateUserRequest.languageCode?.let { userEntity.languageCode = it }
        updateUserRequest.type?.let { userEntity.type = it }
        updateUserRequest.status?.let { userEntity.status = it }
        userEntity.updateTime = System.currentTimeMillis()

        return repository.save(userEntity).toUser()
    }

    private fun UserEntity.toUser(): User = User(
        userId = this.userId,
        email = this.email,
        authId = this.authId,
        authProvider = this.authProvider,
        name = this.name,
        nickname = this.nickname,
        gender = this.gender,
        birthday = this.birthday,
        phoneNumber = this.phoneNumber,
        profileImage = this.profileImage,
        countryCode = this.countryCode,
        languageCode = this.languageCode,
        type = this.type,
        status = this.status,
        createTime = this.createTime,
        updateTime = this.updateTime,
    )

    companion object {
        @JvmStatic
        fun userNotFoundException() = BaseException(404, "user not found")
    }
}
