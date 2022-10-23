package com.housetainer.common.wiremock

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.matching.StringValuePattern
import com.housetainer.common.utils.Constants
import com.housetainer.common.wiremock.rules.NaverMock

import java.util.concurrent.ConcurrentHashMap

import static com.github.tomakehurst.wiremock.client.WireMock.matching
import static com.github.tomakehurst.wiremock.client.WireMock.okJson

class BaseWireMockRule extends WireMockRule {

    private static int NAVER_PORT = 30001

    private static final ObjectMapper objectMapper = new ObjectMapper()
    private static final ConcurrentHashMap<Integer, BaseWireMockRule> mockServers = new ConcurrentHashMap<>()
    private static final String uuidRegex =
        "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}"

    private final int serverPort;

    BaseWireMockRule(int port) {
        super(port)
        this.serverPort = port
    }

    String getLocalHostUrl() {
        return "http://localhost:$serverPort"
    }

    MappingBuilder withTraceIdHeader(MappingBuilder mappingBuilder) {
        return mappingBuilder.withHeader(Constants.API_HEADER_TRACE_ID, matching(uuidRegex))
    }

    String bearerToken(String token) {
        "Bearer $token"
    }

    String toJson(json) {
        objectMapper.writeValueAsString(json)
    }

    ResponseDefinitionBuilder successJson(response) {
        okJson(toJson(response))
    }

    void addQueryParams(MappingBuilder mappingBuilder, Map<String, StringValuePattern> queryParams) {
        if (queryParams != null) {
            queryParams.entrySet().each {
                mappingBuilder.withQueryParam(it.key, it.value)
            }
        }
    }

    // ========== Mock Servers ============

    static List<BaseWireMockRule> getMockServers() {
        mockServers.values().collect()
    }

    static NaverMock getNaverServer() {
        createMockServer(NaverMock, NAVER_PORT)
    }

    static <T extends BaseWireMockRule> T createMockServer(Class<T> clazz, Integer port) {
        mockServers.computeIfAbsent(port) {
            def mockServer = clazz.newInstance(port)
            mockServer.start()
            mockServer
        } as T
    }
}
