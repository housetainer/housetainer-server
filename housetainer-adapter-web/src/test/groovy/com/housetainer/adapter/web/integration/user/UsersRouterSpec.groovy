package com.housetainer.adapter.web.integration.user

import com.housetainer.adapter.web.integration.WebAdapterSpecification
import com.housetainer.common.utils.Constants
import com.housetainer.domain.entity.auth.AuthProvider
import com.housetainer.domain.entity.user.UserStatus
import com.housetainer.domain.model.supporter.UpdateUserRequestBuilder
import com.housetainer.domain.model.supporter.UserBuilder
import com.housetainer.domain.model.user.InternalUpdateUserRequest
import com.housetainer.domain.model.user.UserResponse

class UsersRouterSpec extends WebAdapterSpecification {

    def "PATCH /users - 200"() {
        given:
        def userId = uuid
        def request = UpdateUserRequestBuilder.create()
            .nickname("nick-${uuid.substring(0, 5)}")
            .toUpdateUserRequest()
        def userResponse = UserBuilder.create(
            userId, "email@email.com", uuid, AuthProvider.NAVER, "name", UserStatus.ACTIVE
        ).nickname(request.nickname)
            .toUserResponse()
        def userToken = createUserToken(userResponse)

        when:
        def result = webTestClient
            .patch()
            .uri("/users")
            .header(Constants.AUTHORIZATION, "${Constants.BEARER_PREFIX}$userToken")
            .bodyValue(request)
            .exchange()

        then:
        result.expectStatus().isOk()
        with(extractBody(result, UserResponse)) {
            it == userResponse
        }
        1 * updateUserUseCase.updateUser({ InternalUpdateUserRequest it ->
            it.userId == userId
            it.nickname == request.nickname
        }, _) >> userResponse
        0 * _
    }

    def "PATCH /users - 401 token required"() {
        given:
        def userId = uuid
        def request = UpdateUserRequestBuilder.create()
            .nickname("nick-${uuid.substring(0, 5)}")
            .toUpdateUserRequest()

        when:
        def result = webTestClient
            .patch()
            .uri("/users")
            .bodyValue(request)
            .exchange()

        then:
        result.expectStatus().isUnauthorized()
        0 * _
    }
}
