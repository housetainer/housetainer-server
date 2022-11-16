package com.housetainer.adapter.persistence.integration.webclient

import com.housetainer.adapter.persistence.integration.PersistenceModuleSpecification
import com.housetainer.adapter.persistence.webclient.client.DummyClient
import org.springframework.beans.factory.annotation.Autowired

class DummyClientSpec extends PersistenceModuleSpecification {

    @Autowired
    DummyClient dummyClient

    def setup() {

    }
}
