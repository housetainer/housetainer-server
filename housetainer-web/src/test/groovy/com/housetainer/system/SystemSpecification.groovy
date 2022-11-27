package com.housetainer.system

import com.housetainer.common.utils.Constants
import com.housetainer.common.wiremock.WireMockBaseSpecification
import com.housetainer.domain.entity.auth.AuthProvider
import com.housetainer.domain.entity.exception.BaseException
import com.housetainer.domain.model.auth.SignUpRequest
import com.housetainer.domain.model.user.UserResponse
import org.apache.commons.lang3.tuple.Pair
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Shared

import java.time.Duration

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = SystemTestContext)
@AutoConfigureWebTestClient
abstract class SystemSpecification extends WireMockBaseSpecification {

    @Shared
    Random random = new Random()

    @Autowired
    ApplicationContext context

    @Autowired
    WebTestClient webTestClient

    def setup() {
        webTestClient = WebTestClient.bindToApplicationContext(context)
            .configureClient()
            .responseTimeout(Duration.ofMillis(60000))
            .build()
    }

    public <T> T extractBody(WebTestClient.ResponseSpec results, Class<T> clazz) {
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

    Pair<UserResponse, String> createUser() {
        def results = webTestClient
            .post()
            .uri("/sign/up")
            .bodyValue(new SignUpRequest(
                "$uuid@test.com",
                uuid,
                AuthProvider.GOOGLE,
                "name",
                "nickname-${uuid.substring(0, 5)}",
                "M",
                "1990-01-01",
                randomPhoneNumber(),
                "profileImage",
                "KO",
                "ko_kr",
            ))
            .exchange()
            .expectStatus()
            .isOk()

        String userToken
        results.expectHeader().value(Constants.HOUSETAINER_TOKEN_HEADER) {
            assert it != null
            userToken = it
        }

        def user = extractBody(results, UserResponse)

        return Pair.of(user, userToken)
    }

    def randomPhoneNumber() {
        "010-${String.format("%04d", random.nextInt(10000))}-${String.format("%04d", random.nextInt(10000))}"
    }
}
