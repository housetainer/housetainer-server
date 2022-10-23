package com.housetainer.adapter.persistence.http.webclient.factory

import com.housetainer.adapter.persistence.http.webclient.factory.properties.WebClientProperties
import org.springframework.web.reactive.function.client.WebClient

class WebClientPair(
    val webClient: WebClient,
    val properties: WebClientProperties
)
