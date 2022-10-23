package com.housetainer.domain.model.auth

import com.housetainer.domain.entity.auth.AuthProvider

data class SignInRequest(
    val accessToken: String,
    val authProvider: AuthProvider,
    val userId: String
)
