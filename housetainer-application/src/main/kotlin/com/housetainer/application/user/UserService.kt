package com.housetainer.application.user

import com.housetainer.common.log.logger
import com.housetainer.domain.entity.exception.BaseException
import com.housetainer.domain.entity.user.User
import com.housetainer.domain.model.user.CreateUserRequest
import com.housetainer.domain.model.user.UpdateUserRequest
import com.housetainer.domain.model.user.UserResponse
import com.housetainer.domain.model.user.UserResponse.Companion.toUserResponse
import com.housetainer.domain.persistence.user.CreateUserCommand
import com.housetainer.domain.persistence.user.GetUserByIdQuery
import com.housetainer.domain.persistence.user.UpdateUserCommand
import com.housetainer.domain.usecase.user.CreateUserUseCase
import com.housetainer.domain.usecase.user.UpdateUserUseCase

class UserService(
    private val createUserCommand: CreateUserCommand,
    private val getUserByIdQuery: GetUserByIdQuery,
    private val updateUserCommand: UpdateUserCommand
) : CreateUserUseCase, UpdateUserUseCase {

    private val log = logger()

    override suspend fun createUser(createUserRequest: CreateUserRequest): User {
        log.info("create-user-start")
        return createUserCommand.createUser(createUserRequest)
            .apply {
                log.info("create-user-successfully, userId={}, email={}", userId, email)
            }
    }

    override suspend fun updateUser(updateUserRequest: UpdateUserRequest): UserResponse {
        val user = getUserByIdQuery.getUserById(updateUserRequest.userId) ?: throw userNotFoundException()
        val newRequest = UpdateUserRequest(userId = user.userId)

        if (user.nickname != updateUserRequest.nickname) {
            newRequest.nickname = updateUserRequest.nickname
        }
        if (user.gender != updateUserRequest.gender) {
            newRequest.gender = updateUserRequest.gender
        }
        if (user.birthday != updateUserRequest.birthday) {
            newRequest.birthday = updateUserRequest.birthday
        }
        if (user.phoneNumber != updateUserRequest.phoneNumber) {
            newRequest.phoneNumber = updateUserRequest.phoneNumber
        }
        if (user.profileImage != updateUserRequest.profileImage) {
            newRequest.profileImage = updateUserRequest.profileImage
        }
        if (user.countryCode != updateUserRequest.countryCode) {
            newRequest.countryCode = updateUserRequest.countryCode
        }
        if (user.languageCode != updateUserRequest.languageCode) {
            newRequest.languageCode = updateUserRequest.languageCode
        }
        if (user.type != updateUserRequest.type) {
            newRequest.type = updateUserRequest.type
        }
        if (user.status != updateUserRequest.status) {
            newRequest.status = updateUserRequest.status
        }

        val isUpdated = listOfNotNull(
            newRequest.nickname,
            newRequest.gender,
            newRequest.birthday,
            newRequest.phoneNumber,
            newRequest.profileImage,
            newRequest.countryCode,
            newRequest.languageCode,
            newRequest.type,
            newRequest.status
        ).isNotEmpty()

        return if (isUpdated) {
            updateUserCommand.updateUser(newRequest)
        } else {
            user
        }.toUserResponse()
    }

    companion object {
        @JvmStatic
        fun userNotFoundException() = BaseException(404, "user not found")
    }
}
