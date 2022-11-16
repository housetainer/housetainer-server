package com.housetainer.domain.entity.auth

data class TokenInformation(
    val userId: String,
    val authId: String,
    val authProvider: AuthProvider
)
