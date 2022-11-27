package com.housetainer.adapter.web.integration

import com.housetainer.common.BaseSpecification
import com.housetainer.domain.entity.auth.AuthProvider
import com.housetainer.domain.entity.exception.BaseException
import com.housetainer.domain.entity.user.UserStatus
import com.housetainer.domain.entity.user.UserType
import com.housetainer.domain.model.auth.InternalIssueTokenRequest
import com.housetainer.domain.model.user.UserResponse
import com.housetainer.domain.port.token.TokenService
import com.housetainer.domain.usecase.auth.SignUseCase
import com.housetainer.domain.usecase.invitation.RegisterInvitationUseCase
import com.housetainer.domain.usecase.user.UpdateUserUseCase
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Shared

import java.time.Duration

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ContextConfiguration(initializers = WebAdapterContext.class)
class WebAdapterSpecification extends BaseSpecification {

    @Shared
    TokenService tokenService = TokenService.INSTANCE.getInstance(uuid, Duration.ofMinutes(1L))

    @Autowired
    WebTestClient webTestClient

    @SpringBean
    SignUseCase signUseCase = Mock()

    @SpringBean
    UpdateUserUseCase updateUserUseCase = Mock()

    @SpringBean
    RegisterInvitationUseCase registerInvitationUseCase = Mock()

    static <T> T extractBody(WebTestClient.ResponseSpec results, Class<T> clazz) {
        results.expectBody(clazz).returnResult().responseBody
    }

    void extractExceptionAndCompare(
        WebTestClient.ResponseSpec results,
        BaseException expectedException
    ) {
        def exception = results.expectBody(BaseException).returnResult().responseBody
        assert exception.code == expectedException.code
        assert exception.message == expectedException.message
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

    def createUserToken(UserResponse userResponse) {
        tokenService.issueToken(new InternalIssueTokenRequest(
            userResponse.userId, userResponse.authId, userResponse.authProvider
        ))
    }
}
