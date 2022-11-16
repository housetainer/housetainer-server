package com.housetainer.adapter.persistence.webclient.client

import com.housetainer.adapter.persistence.http.webclient.factory.WebClientPair
import com.housetainer.adapter.persistence.webclient.filter.WebClientFilter
import com.housetainer.common.log.logger
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component
import reactor.util.retry.Retry

@Component
@DependsOn("webClientFactory")
class DummyClient(
    @Qualifier("naverWebClientPair") webClientPair: WebClientPair
) {

    private val log = logger()

    private val webClient = webClientPair.webClient

    @Suppress("UnusedPrivateMember")
    private val retrySpec = Retry.backoff(
        webClientPair.properties.maxRetry, webClientPair.properties.retryDelay
    ).filter { WebClientFilter.is5xxServerError(it) }
}
