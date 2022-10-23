package com.housetainer.domain.usecase.auth

import com.housetainer.domain.entity.user.User
import com.housetainer.domain.model.auth.SignInRequest
import com.housetainer.domain.model.auth.SignUpRequest

interface SignUseCase {

    suspend fun signUp(signUpRequest: SignUpRequest): User

    suspend fun signIn(signInRequest: SignInRequest): User
}
