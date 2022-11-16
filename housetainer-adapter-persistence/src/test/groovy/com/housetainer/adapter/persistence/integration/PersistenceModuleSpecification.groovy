package com.housetainer.adapter.persistence.integration

import com.housetainer.common.wiremock.WireMockBaseSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient

import java.time.Duration

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@ContextConfiguration(initializers = PersistenceModuleContext.class)
class PersistenceModuleSpecification extends WireMockBaseSpecification {

    @Autowired
    ApplicationContext context

    @Autowired
    WebTestClient webTestClient

    def setup() {
        webTestClient = WebTestClient.bindToApplicationContext(context)
            .configureClient()
            .responseTimeout(Duration.ofMillis(600000))
            .build()
    }

}
