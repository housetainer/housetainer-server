package com.housetainer.common.wiremock.rules

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.housetainer.common.utils.Constants
import com.housetainer.common.wiremock.BaseWireMockRule

import static com.github.tomakehurst.wiremock.client.WireMock.*

class NaverMock extends BaseWireMockRule {

    NaverMock(int port) {
        super(port)
    }

    void "GET_v1_nid_me"(String accessToken, ResponseDefinitionBuilder response) {
        addStubMapping(
            get(urlEqualTo('/v1/nid/me'))
                .withHeader(Constants.AUTHORIZATION, equalTo(bearerToken(accessToken)))
                .willReturn(response)
                .build()
        )
    }
}
