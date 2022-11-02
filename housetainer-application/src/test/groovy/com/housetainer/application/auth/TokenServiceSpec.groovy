package com.housetainer.application.auth

import com.housetainer.application.ApplicationSpecification
import com.housetainer.domain.entity.auth.AuthProvider
import com.housetainer.domain.entity.exception.BaseException
import com.housetainer.domain.model.auth.IssueTokenRequest
import spock.lang.Shared

import java.time.Duration

class TokenServiceSpec extends ApplicationSpecification {

    @Shared
    String secretKey = uuid

    @Shared
    Duration timeout = Duration.ofSeconds(3)

    TokenService tokenService

    def setup() {
        tokenService = new TokenService(secretKey, timeout)
    }

    def "issue and validate token"() {
        given:
        def userId = uuid
        def authId = uuid
        def authProvider = AuthProvider.NAVER
        def request = new IssueTokenRequest(userId, authId, authProvider)

        when:
        def token = tokenService.issueToken(request)

        then:
        token != null
        0 * _

        when:
        def tokenInformation = tokenService.validateToken(token)

        then:
        tokenInformation != null
        tokenInformation.userId == userId
        tokenInformation.authId == authId
        tokenInformation.authProvider == authProvider
        0 * _
    }

    def "token expired"() {
        given:
        def userId = uuid
        def authId = uuid
        def authProvider = AuthProvider.NAVER
        def request = new IssueTokenRequest(userId, authId, authProvider)

        when:
        def token = tokenService.issueToken(request)

        then:
        token != null
        0 * _

        when:
        sleep(timeout.toMillis() + 100)
        tokenService.validateToken(token)

        then:
        def exception = thrown(BaseException)
        exception == TokenService.tokenExpiredException()
    }

    def "invalid token"() {
        when:
        tokenService.validateToken(uuid)

        then:
        def exception = thrown(BaseException)
        exception == TokenService.tokenMalformedException()
    }
}
