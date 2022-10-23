package com.housetainer.common.wiremock

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.housetainer.common.BaseSpecification
import com.housetainer.common.wiremock.rules.NaverMock
import org.junit.ClassRule
import spock.lang.Shared

import static com.github.tomakehurst.wiremock.client.WireMock.okJson

abstract class WireMockBaseSpecification extends BaseSpecification {

    private static final ObjectMapper objectMapper = new ObjectMapper()

    @ClassRule
    @Shared
    NaverMock naver = BaseWireMockRule.naverServer

    def cleanup() {
        BaseWireMockRule.mockServers.each {
            it.resetAll()
        }
    }

    void verifyNoMissingStubs() {
        BaseWireMockRule.mockServers.each {
            assert !it.findUnmatchedRequests().requests
        }
    }

    String bearerToken(token) {
        "Bearer $token"
    }

    String toJson(json) {
        objectMapper.writeValueAsString(json)
    }

    ResponseDefinitionBuilder successJson(response) {
        okJson(toJson(response))
    }

}
