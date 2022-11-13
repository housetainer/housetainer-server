package com.housetainer.domain.model.auth

import com.housetainer.domain.model.user.UserResponse

data class InternalSignUpResult(
    val user: UserResponse,
    val token: String
)
