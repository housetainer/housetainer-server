package com.housetainer.system


import com.housetainer.common.wiremock.WireMockBaseSpecification
import com.housetainer.domain.entity.exception.BaseException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient

import java.time.Duration

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = SystemTestContext)
@AutoConfigureWebTestClient
abstract class SystemSpecification extends WireMockBaseSpecification {

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
}
