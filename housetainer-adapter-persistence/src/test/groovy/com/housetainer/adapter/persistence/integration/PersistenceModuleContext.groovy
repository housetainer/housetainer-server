package com.housetainer.adapter.persistence.integration

import com.housetainer.common.wiremock.BaseWireMockRule
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.support.TestPropertySourceUtils

class PersistenceModuleContext implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    void initialize(ConfigurableApplicationContext applicationContext) {
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext,
            "webClient.defaultProperties.maxRetry:0",
            "webClient.services.naver.url:${BaseWireMockRule.naverServer.localHostUrl}",
        )
    }
}
