package com.housetainer.system.sign

import com.housetainer.common.utils.Constants
import com.housetainer.domain.entity.auth.AuthProvider
import com.housetainer.domain.entity.user.UserStatus
import com.housetainer.domain.entity.user.UserType
import com.housetainer.domain.model.auth.RenewTokenRequest
import com.housetainer.domain.model.auth.SignUpRequest
import com.housetainer.domain.model.user.UserResponse
import com.housetainer.system.SystemSpecification
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class SignSpec extends SystemSpecification {

    @Shared
    Random random = new Random()

    @Shared
    UserResponse user

    @Shared
    String userToken

    def "sign up"() {
        given:
        def request = new SignUpRequest(
            "$uuid@test.com",
            uuid,
            AuthProvider.NAVER,
            "name",
            "nickname",
            "M",
            "2020-01-01",
            randomPhoneNumber(),
            "profileImage",
            "countryCode",
            "languageCode",
        )

        when:
        def results = webTestClient
            .post()
            .uri("/sign/up")
            .bodyValue(request)
            .exchange()

        then:
        results.expectStatus().isOk()
        def userResponse = extractBody(results, UserResponse)
        userResponse != null
        userResponse.status == UserStatus.ACTIVE
        userResponse.type == UserType.MEMBER
        results.expectHeader().value(Constants.HOUSETAINER_TOKEN_HEADER) {
            assert it != null
            userToken = it
        }
        verifyNoMissingStubs()
        0 * _

        cleanup:
        user = userResponse
    }

    def "sing in"() {
        when:
        def results = webTestClient
            .post()
            .uri("/sign/in")
            .header(Constants.AUTHORIZATION, "${Constants.BEARER_PREFIX}$userToken")
            .exchange()

        then:
        results.expectStatus().isOk()
        with(extractBody(results, UserResponse)) {
            user == it
        }
        verifyNoMissingStubs()
        0 * _
    }

    def "sing in with invalid token"() {
        when:
        def results = webTestClient
            .post()
            .uri("/sign/in")
            .header(Constants.AUTHORIZATION, "${Constants.BEARER_PREFIX}$uuid")
            .exchange()

        then:
        results.expectStatus().isUnauthorized()
        verifyNoMissingStubs()
        0 * _
    }

    def "renew token"() {
        given:
        def request = new RenewTokenRequest(
            null, user.userId, user.authId, user.authProvider
        )

        when:
        def results = webTestClient
            .post()
            .uri("/sign/token/renew")
            .bodyValue(request)
            .exchange()

        then:
        results.expectStatus().isNoContent()
        results.expectHeader().value(Constants.HOUSETAINER_TOKEN_HEADER) {
            assert it != null
            userToken = it
        }
        verifyNoMissingStubs()
        0 * _
    }

    def randomPhoneNumber() {
        "010-${String.format("%04d", random.nextInt(10000))}-${String.format("%04d", random.nextInt(10000))}"
    }
}
