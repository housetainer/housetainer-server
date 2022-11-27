package com.housetainer.system.invitation


import com.housetainer.domain.model.invitation.RegisterInvitationRequest
import com.housetainer.system.SystemSpecification

class InvitationSpec extends SystemSpecification {

    def "POST /invitation/register - 200"() {
        given:
        def request = new RegisterInvitationRequest("123456")

        when:
        def result = webTestClient
            .post()
            .uri("/invitation/register")
            .bodyValue(request)
            .exchange()

        then:
        result.expectStatus().isEqualTo(202)
        verifyNoMissingStubs()
    }

    def "POST /invitation/register - 403"() {
        given:
        def request = new RegisterInvitationRequest("000000")

        when:
        def result = webTestClient
            .post()
            .uri("/invitation/register")
            .bodyValue(request)
            .exchange()

        then:
        result.expectStatus().isForbidden()
        verifyNoMissingStubs()
    }
}
