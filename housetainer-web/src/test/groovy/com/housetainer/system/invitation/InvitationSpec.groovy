package com.housetainer.system.invitation


import com.housetainer.system.SystemSpecification

class InvitationSpec extends SystemSpecification {

    def "POST /invitation/register - 200"() {
        given:
        def code = "123456"

        when:
        def result = webTestClient
            .post()
            .uri("/invitation/$code/approve")
            .exchange()

        then:
        result.expectStatus().isEqualTo(202)
        verifyNoMissingStubs()
    }

    def "POST /invitation/register - 403"() {
        given:
        def code = "000000"

        when:
        def result = webTestClient
            .post()
            .uri("/invitation/$code/approve")
            .exchange()

        then:
        result.expectStatus().isForbidden()
        verifyNoMissingStubs()
    }
}
