package com.housetainer.domain.model.auth

import com.housetainer.domain.entity.auth.AuthProvider

data class InternalIssueTokenRequest(
    val userId: String,
    val authId: String,
    val authProvider: AuthProvider
)
