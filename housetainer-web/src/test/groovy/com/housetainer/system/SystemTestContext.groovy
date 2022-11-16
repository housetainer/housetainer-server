package com.housetainer.system

import com.housetainer.common.wiremock.BaseWireMockRule
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.support.TestPropertySourceUtils

class SystemTestContext implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    static String userTokenSecret = UUID.randomUUID().toString()

    @Override
    void initialize(ConfigurableApplicationContext applicationContext) {
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext,
            "webClient.defaultProperties.maxRetry:0",
            "webClient.services.naver.url:${BaseWireMockRule.naverServer.localHostUrl}",

            "auth.userToken.secretKey:$userTokenSecret",
        )
    }
}
