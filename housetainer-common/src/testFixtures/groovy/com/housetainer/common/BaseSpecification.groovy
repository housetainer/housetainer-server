package com.housetainer.common

import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import spock.lang.Shared
import spock.lang.Specification

class BaseSpecification extends Specification {

    @Shared
    Continuation coroutineContext = Mock() {
        _ * getContext() >> Mock(CoroutineContext)
    }

    String getUuid() {
        UUID.randomUUID().toString()
    }

    List<String> buildUuidList(size) {
        (1..size).collect { uuid }
    }
}
