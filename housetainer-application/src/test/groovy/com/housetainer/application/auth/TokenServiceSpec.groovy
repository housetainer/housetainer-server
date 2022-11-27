package com.housetainer.application.auth

import com.housetainer.application.ApplicationSpecification
import com.housetainer.domain.entity.auth.AuthProvider
import com.housetainer.domain.entity.exception.BaseException
import com.housetainer.domain.model.auth.InternalIssueTokenRequest
import com.housetainer.domain.port.token.TokenService
import spock.lang.Shared

import java.time.Duration

class TokenServiceSpec extends ApplicationSpecification {

    @Shared
    Duration specTimeout = Duration.ofSeconds(3L)

    def setupSpec() {
        tokenService = TokenService.INSTANCE.getInstance(secretKey, specTimeout)
    }

    def cleanupSpec() {
        tokenService = TokenService.INSTANCE.getInstance(secretKey, timeout)
    }

    def "issue and validate token"() {
        given:
        def userId = uuid
        def authId = uuid
        def authProvider = AuthProvider.NAVER
        def request = new InternalIssueTokenRequest(userId, authId, authProvider)

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
        def request = new InternalIssueTokenRequest(userId, authId, authProvider)

        when:
        def token = tokenService.issueToken(request)

        then:
        token != null
        0 * _

        when:
        sleep(specTimeout.toMillis() + 100)
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
