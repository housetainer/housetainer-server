package com.housetainer.system.user

import com.housetainer.common.utils.Constants
import com.housetainer.domain.entity.user.UserType
import com.housetainer.domain.model.supporter.UpdateUserRequestBuilder
import com.housetainer.domain.model.user.UserResponse
import com.housetainer.system.SystemSpecification
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class UserSpec extends SystemSpecification {

    @Shared
    UserResponse user

    @Shared
    String userToken

    def setup() {
        if (user == null) {
            def pair = createUser()
            user = pair.left
            userToken = pair.right
        }
    }

    def "PATCH /users - 200"() {
        given:
        def request = UpdateUserRequestBuilder.create(user.userId)
            .type(UserType.HOUSETAINER)
            .profileImage("profileImage2")
            .toUpdateUserRequest()

        when:
        def result = webTestClient
            .patch()
            .uri("/users")
            .header(Constants.AUTHORIZATION, "${Constants.BEARER_PREFIX}$userToken")
            .bodyValue(request)
            .exchange()

        then:
        result.expectStatus().isOk()
        def responseUser = extractBody(result, UserResponse)
        responseUser.userId == user.userId
        responseUser.type == request.type
        responseUser.profileImage == request.profileImage
        verifyNoMissingStubs()

        cleanup:
        user = responseUser
    }

    def "PATCH /users - 409 nickname duplicate"() {
        given:
        def otherUser = createUser().left
        def request = UpdateUserRequestBuilder.create(user.userId)
            .nickname(otherUser.nickname)
            .toUpdateUserRequest()

        when:
        def result = webTestClient
            .patch()
            .uri("/users")
            .header(Constants.AUTHORIZATION, "${Constants.BEARER_PREFIX}$userToken")
            .bodyValue(request)
            .exchange()

        then:
        result.expectStatus().isEqualTo(409)
        verifyNoMissingStubs()
    }
}
