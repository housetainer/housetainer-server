package com.housetainer.application.auth

import com.housetainer.domain.entity.exception.BaseException
import com.housetainer.domain.entity.user.User
import com.housetainer.domain.entity.user.UserStatus
import com.housetainer.domain.entity.user.UserType
import com.housetainer.domain.model.auth.InternalIssueTokenRequest
import com.housetainer.domain.model.auth.InternalSignUpResult
import com.housetainer.domain.model.auth.RenewTokenRequest
import com.housetainer.domain.model.auth.SignUpRequest
import com.housetainer.domain.model.user.CreateUserRequest
import com.housetainer.domain.model.user.UserResponse
import com.housetainer.domain.persistence.user.GetUserByEmailQuery
import com.housetainer.domain.persistence.user.GetUserByIdQuery
import com.housetainer.domain.port.token.TokenService
import com.housetainer.domain.usecase.auth.SignUseCase
import com.housetainer.domain.usecase.user.CreateUserUseCase
import java.time.Duration

class SignService(
    private val getUserByIdQuery: GetUserByIdQuery,
    private val getUserByEmailQuery: GetUserByEmailQuery,
    private val createUserUseCase: CreateUserUseCase,
    tokenSecretKey: String,
    tokenTimeout: Duration
) : SignUseCase {

    private val tokenService = TokenService.getInstance(tokenSecretKey, tokenTimeout)

    override suspend fun signUp(signUpRequest: SignUpRequest): InternalSignUpResult {
        val user = (getUserByEmailQuery.getUserByEmail(signUpRequest.email) ?: createUser(signUpRequest))
        val token = tokenService.issueToken(user.toIssueTokenRequest())

        return InternalSignUpResult(user.toUserResponse(), token)
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

    override suspend fun signIn(token: String): UserResponse {
        val tokenInformation = tokenService.validateToken(token)

        return getUserByIdQuery.getUserById(tokenInformation.userId)
            ?.toUserResponse()
            ?: throw userNotFoundException()
    }

    override suspend fun renewToken(renewTokenRequest: RenewTokenRequest): String {
        val user: User = (
            if (renewTokenRequest.userId != null) {
                getUserByIdQuery.getUserById(renewTokenRequest.userId!!)
            } else if (renewTokenRequest.email != null) {
                getUserByEmailQuery.getUserByEmail(renewTokenRequest.email!!)
            } else {
                throw userIdOrEmailRequiredException()
            }
            ) ?: throw userNotFoundException()

        return tokenService.issueToken(user.toIssueTokenRequest())
    }

    private fun User.toIssueTokenRequest() = InternalIssueTokenRequest(
        userId, authId, authProvider
    )

    private fun User.toUserResponse() = UserResponse(
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
        updateTime = this.updateTime
    )

    companion object {
        @JvmStatic
        fun userNotFoundException() = BaseException(404, "user not found")

        @JvmStatic
        fun userIdOrEmailRequiredException() = BaseException(400, "userId or email is required")
    }
}
