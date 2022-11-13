package com.housetainer.adapter.web.integration

import com.housetainer.common.BaseSpecification
import com.housetainer.domain.entity.exception.BaseException
import com.housetainer.domain.usecase.auth.SignUseCase
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ContextConfiguration(initializers = WebAdapterContext.class)
class WebAdapterSpecification extends BaseSpecification {

    @Autowired
    WebTestClient webTestClient

    @SpringBean
    SignUseCase signUseCase = Mock()


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
}
