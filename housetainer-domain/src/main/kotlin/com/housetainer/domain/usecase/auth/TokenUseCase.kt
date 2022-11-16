package com.housetainer.domain.usecase.auth

import com.housetainer.domain.entity.auth.TokenInformation
import com.housetainer.domain.model.auth.InternalIssueTokenRequest

interface TokenUseCase {
    fun issueToken(issueTokenRequest: InternalIssueTokenRequest): String

    fun validateToken(token: String): TokenInformation
}
