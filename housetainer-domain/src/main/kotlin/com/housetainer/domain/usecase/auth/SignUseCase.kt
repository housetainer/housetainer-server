package com.housetainer.domain.usecase.auth

import com.housetainer.domain.entity.auth.TokenInformation
import com.housetainer.domain.model.auth.InternalSignUpResult
import com.housetainer.domain.model.auth.RenewTokenRequest
import com.housetainer.domain.model.auth.SignUpRequest
import com.housetainer.domain.model.user.UserResponse

interface SignUseCase {

    suspend fun signUp(signUpRequest: SignUpRequest): InternalSignUpResult

    suspend fun signIn(tokenInformation: TokenInformation): UserResponse

    suspend fun renewToken(renewTokenRequest: RenewTokenRequest): String
}
