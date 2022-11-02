package com.housetainer.domain.model.auth

import com.housetainer.domain.entity.auth.AuthProvider

data class IssueTokenRequest(
    val userId: String,
    val authId: String,
    val authProvider: AuthProvider
)
