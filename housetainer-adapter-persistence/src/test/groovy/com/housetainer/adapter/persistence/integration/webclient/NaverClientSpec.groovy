package com.housetainer.adapter.persistence.integration.webclient

import com.housetainer.adapter.persistence.integration.PersistenceModuleSpecification
import com.housetainer.adapter.persistence.model.NaverUserProfile
import com.housetainer.adapter.persistence.model.NaverUserProfileResponse
import com.housetainer.adapter.persistence.webclient.client.NaverClient
import com.housetainer.common.CoroutineTestUtils
import com.housetainer.domain.model.auth.UserProfileResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.WebClientResponseException

import static com.github.tomakehurst.wiremock.client.WireMock.*

class NaverClientSpec extends PersistenceModuleSpecification {

    @Autowired
    NaverClient naverClient

    def setup() {

    }

    def "get user profile"() {
        given:
        def accessToken = uuid
        def profile = new UserProfileResponse(
            "10",
            "test@test.com",
            "name",
            "nickname",
            "F",
            "2000-01-01",
            "http://profile-image.jpg",
            "010-1234-5678"
        )
        def naverUserProfile = new NaverUserProfile(
            "200",
            "success",
            new NaverUserProfileResponse(
                profile.authId,
                profile.email,
                profile.name,
                profile.nickname,
                profile.gender,
                null,
                profile.birthday,
                profile.profileImage,
                profile.phoneNumber
            )
        )
        naver.GET_v1_nid_me(accessToken, successJson(naverUserProfile))

        when:
        UserProfileResponse result = CoroutineTestUtils.executeSuspendFun {
            naverClient.getUserProfile(accessToken, it)
        }

        then:
        result == profile
        naver.verify(1, getRequestedFor(urlPathEqualTo("/v1/nid/me")))
        verifyNoMissingStubs()
    }

    def "get user profile but 401"() {
        given:
        def accessToken = uuid
        naver.GET_v1_nid_me(accessToken, unauthorized())

        when:
        CoroutineTestUtils.executeSuspendFun {
            naverClient.getUserProfile(accessToken, it)
        }

        then:
        def exception = thrown(WebClientResponseException)
        exception.statusCode == HttpStatus.UNAUTHORIZED
        verifyNoMissingStubs()
    }

}
