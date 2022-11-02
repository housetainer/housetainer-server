package com.housetainer.domain.usecase.auth

import com.housetainer.domain.entity.auth.TokenInformation
import com.housetainer.domain.model.auth.IssueTokenRequest

interface TokenUseCase {
    fun issueToken(issueTokenRequest: IssueTokenRequest): String

    fun validateToken(token: String): TokenInformation
}
