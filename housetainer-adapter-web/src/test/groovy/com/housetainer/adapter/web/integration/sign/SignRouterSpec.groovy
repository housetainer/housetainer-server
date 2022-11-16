package com.housetainer.adapter.web.integration.sign

import com.housetainer.adapter.web.exception.HandlerExceptions
import com.housetainer.adapter.web.integration.WebAdapterSpecification
import com.housetainer.common.utils.Constants
import com.housetainer.domain.entity.auth.AuthProvider
import com.housetainer.domain.entity.user.UserStatus
import com.housetainer.domain.entity.user.UserType
import com.housetainer.domain.model.auth.InternalSignUpResult
import com.housetainer.domain.model.auth.RenewTokenRequest
import com.housetainer.domain.model.auth.SignUpRequest
import com.housetainer.domain.model.user.UserResponse

class SignRouterSpec extends WebAdapterSpecification {

    def "POST /sign/up - 200"() {
        given:
        def userResponse = createUserResponse()
        def token = uuid
        def request = new SignUpRequest(
            userResponse.email,
            userResponse.authId,
            userResponse.authProvider,
            userResponse.name,
            userResponse.nickname,
            userResponse.gender,
            userResponse.birthday,
            userResponse.phoneNumber,
            userResponse.profileImage,
            userResponse.countryCode,
            userResponse.languageCode
        )

        when:
        def result = webTestClient
            .post()
            .uri("/sign/up")
            .bodyValue(request)
            .exchange()

        then:
        result.expectStatus().isOk()
        with(extractBody(result, UserResponse)) {
            it == userResponse
        }
        result.expectHeader().valueEquals(Constants.HOUSETAINER_TOKEN_HEADER, token)
        1 * signUseCase.signUp(request, _) >> new InternalSignUpResult(userResponse, token)
        0 * _
    }

    def "POST /sign/up - 400 empty body"() {
        when:
        def result = webTestClient
            .post()
            .uri("/sign/up")
            .exchange()

        then:
        result.expectStatus().isBadRequest()
        extractExceptionAndCompare(result, HandlerExceptions.emptyBodyException())
        0 * _
    }

    def "POST /sign/in - 200"() {
        given:
        def userResponse = createUserResponse()
        def token = uuid

        when:
        def result = webTestClient
            .post()
            .uri("/sign/in")
            .header(Constants.AUTHORIZATION, Constants.BEARER_PREFIX + "$token")
            .exchange()

        then:
        result.expectStatus().isOk()
        with(extractBody(result, UserResponse)) {
            it == userResponse
        }
        1 * signUseCase.signIn(token, _) >> userResponse
        0 * _
    }

    def "POST /sign/token/renew - 200"() {
        given:
        def request = new RenewTokenRequest(
            null, uuid, uuid, AuthProvider.NAVER
        )
        def token = uuid

        when:
        def result = webTestClient
            .post()
            .uri("/sign/token/renew")
            .bodyValue(request)
            .exchange()

        then:
        result.expectStatus().isNoContent()
        result.expectHeader().valueEquals(Constants.HOUSETAINER_TOKEN_HEADER, token)
        1 * signUseCase.renewToken(request, _) >> token
        0 * _
    }

    def "POST /sign/token/renew - 400 empty body"() {
        when:
        def result = webTestClient
            .post()
            .uri("/sign/token/renew")
            .exchange()

        then:
        result.expectStatus().isBadRequest()
        0 * _
    }

    def createUserResponse(String userId = uuid) {
        new UserResponse(
            userId,
            "test@test.com",
            uuid,
            AuthProvider.NAVER,
            "name",
            "nickname",
            "M",
            "2020-01-01",
            "phoneNumber",
            "profileImage",
            "countryCode",
            "languageCode",
            UserType.MEMBER,
            UserStatus.ACTIVE,
            System.currentTimeMillis(),
            System.currentTimeMillis()
        )
    }
}
