package com.housetainer.application.auth

import com.housetainer.domain.entity.auth.AuthProvider
import com.housetainer.domain.entity.exception.BaseException
import com.housetainer.domain.entity.user.User
import com.housetainer.domain.entity.user.UserStatus
import com.housetainer.domain.entity.user.UserType
import com.housetainer.domain.model.auth.SignInRequest
import com.housetainer.domain.model.auth.SignUpRequest
import com.housetainer.domain.model.auth.UserProfileResponse
import com.housetainer.domain.model.device.UpsertDeviceRequest
import com.housetainer.domain.model.user.CreateUserRequest
import com.housetainer.domain.persistence.auth.GetUserProfileFromNaverQuery
import com.housetainer.domain.persistence.user.GetUserByEmailQuery
import com.housetainer.domain.usecase.auth.SignUseCase
import com.housetainer.domain.usecase.device.UpsertDeviceUseCase
import com.housetainer.domain.usecase.user.CreateUserUseCase

class SignService(
    private val getUserProfileFromNaverQuery: GetUserProfileFromNaverQuery,
    private val getUserByEmailQuery: GetUserByEmailQuery,
    private val createUserUseCase: CreateUserUseCase,
    private val upsertDeviceUseCase: UpsertDeviceUseCase
) : SignUseCase {

    override suspend fun signUp(signUpRequest: SignUpRequest): User {
        val userProfile = getUserProfile(signUpRequest.accessToken, signUpRequest.authProvider)

        if (userProfile.authId != signUpRequest.authId) {
            throw userUnauthorizedException()
        }

        return (getUserByEmailQuery.getUserByEmail(signUpRequest.email) ?: createUser(signUpRequest))
            .apply {
                upsertDeviceUseCase.upsertDevice(
                    UpsertDeviceRequest(
                        signUpRequest.deviceId,
                        this.userId,
                        signUpRequest.platform,
                        signUpRequest.platformVersion,
                        signUpRequest.appVersion,
                        signUpRequest.locale
                    )
                )
            }
    }

    private suspend fun createUser(signUpRequest: SignUpRequest): User {
        val now = System.currentTimeMillis()
        return createUserUseCase.createUser(
            CreateUserRequest(
                email = signUpRequest.email,
                authId = signUpRequest.authId,
                authProvider = signUpRequest.authProvider,
                name = signUpRequest.name,
                nickname = signUpRequest.nickname,
                gender = signUpRequest.gender,
                birthday = signUpRequest.birthday,
                phoneNumber = signUpRequest.phoneNumber,
                profileImage = signUpRequest.profileImage,
                countryCode = null,
                languageCode = null,
                type = UserType.MEMBER,
                status = UserStatus.ACTIVE,
                createTime = now,
                updateTime = now,
            )
        )
    }

    override suspend fun signIn(signInRequest: SignInRequest): User {
        val userProfile = getUserProfile(signInRequest.accessToken, signInRequest.authProvider)
        val user = getUserByEmailQuery.getUserByEmail(userProfile.email)
        if (user == null) {
            throw userNotFoundException()
        } else {
            return user
        }
    }

    private suspend fun getUserProfile(accessToken: String, authProvider: AuthProvider): UserProfileResponse {
        return when (authProvider) {
            AuthProvider.NAVER -> getUserProfileFromNaverQuery.getUserProfile(accessToken)
            else -> TODO()
        }
    }

    companion object {
        @JvmStatic
        fun userUnauthorizedException() = BaseException(401, "user unauthorized")

        @JvmStatic
        fun userNotFoundException() = BaseException(404, "user not found")
    }
}
