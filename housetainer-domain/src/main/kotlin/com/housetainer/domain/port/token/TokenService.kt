package com.housetainer.domain.port.token

import com.housetainer.common.utils.CommonUtils
import com.housetainer.domain.entity.auth.TokenInformation
import com.housetainer.domain.entity.exception.BaseException
import com.housetainer.domain.model.auth.InternalIssueTokenRequest
import com.housetainer.domain.usecase.auth.TokenUseCase
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import java.time.Duration
import java.util.Date

object TokenService : TokenUseCase {
    private lateinit var secretKey: String
    private lateinit var timeout: Duration

    override fun issueToken(issueTokenRequest: InternalIssueTokenRequest): String {
        val currentMs = System.currentTimeMillis()

        return Jwts.builder()
            .setSubject(issueTokenRequest.userId)
            .claim("body", issueTokenRequest)
            .setIssuedAt(Date(currentMs))
            .setExpiration(Date(currentMs + timeout.toMillis()))
            .signWith(SignatureAlgorithm.HS256, secretKey.toByteArray(Charsets.UTF_8))
            .compact()
    }

    override fun validateToken(token: String): TokenInformation {
        return runCatching {
            val claims = Jwts.parser()
                .setSigningKey(secretKey.toByteArray(Charsets.UTF_8))
                .parseClaimsJws(token)

            CommonUtils.mapper
                .convertValue(claims.body["body"], InternalIssueTokenRequest::class.java)
                .toTokenInformation()
        }.onFailure {
            throw when (it) {
                is ExpiredJwtException -> tokenExpiredException()
                is MalformedJwtException -> tokenMalformedException()
                else -> it
            }
        }.getOrThrow()
    }

    private fun InternalIssueTokenRequest.toTokenInformation() = TokenInformation(userId, authId, authProvider)

    @JvmStatic
    fun tokenExpiredException() = BaseException(401, "token expired")

    @JvmStatic
    fun tokenMalformedException() = BaseException(401, "token malformed")

    fun getInstance(secretKey: String, timeout: Duration): TokenService {
        synchronized(this) {
            if (!::secretKey.isInitialized || this.secretKey != secretKey) {
                this.secretKey = secretKey
            }
            if (!::timeout.isInitialized || this.timeout != timeout) {
                this.timeout = timeout
            }
            return this
        }
    }
}
