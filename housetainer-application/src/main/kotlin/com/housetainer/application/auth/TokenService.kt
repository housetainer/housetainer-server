package com.housetainer.application.auth

import com.housetainer.common.utils.CommonUtils
import com.housetainer.domain.entity.auth.TokenInformation
import com.housetainer.domain.entity.exception.BaseException
import com.housetainer.domain.model.auth.IssueTokenRequest
import com.housetainer.domain.usecase.auth.TokenUseCase
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import java.time.Duration
import java.util.Date

class TokenService(secretKey: String, timeout: Duration) : TokenUseCase {

    private val timeout: Long = timeout.toMillis()
    private val decodedSecretKey = secretKey.toByteArray(Charsets.UTF_8)

    override fun issueToken(issueTokenRequest: IssueTokenRequest): String {
        val currentMs = System.currentTimeMillis()

        return Jwts.builder()
            .setSubject(issueTokenRequest.userId)
            .claim("body", issueTokenRequest)
            .setIssuedAt(Date(currentMs))
            .setExpiration(Date(currentMs + timeout))
            .signWith(SignatureAlgorithm.HS256, decodedSecretKey)
            .compact()
    }

    override fun validateToken(token: String): TokenInformation {
        return runCatching {
            val claims = Jwts.parser()
                .setSigningKey(decodedSecretKey)
                .parseClaimsJws(token)

            CommonUtils.mapper
                .convertValue(claims.body.get("body"), IssueTokenRequest::class.java)
                .toTokenInformation()
        }.onFailure {
            throw when (it) {
                is ExpiredJwtException -> tokenExpiredException()
                is MalformedJwtException -> tokenMalformedException()
                else -> it
            }
        }.getOrThrow()
    }

    private fun IssueTokenRequest.toTokenInformation() = TokenInformation(userId, authId, authProvider)

    companion object {
        @JvmStatic
        fun tokenExpiredException() = BaseException(401, "token expired")

        @JvmStatic
        fun tokenMalformedException() = BaseException(401, "token malformed")
    }
}
