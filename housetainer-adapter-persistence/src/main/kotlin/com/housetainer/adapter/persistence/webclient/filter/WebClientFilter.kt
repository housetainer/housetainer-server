package com.housetainer.adapter.persistence.webclient.filter

import com.housetainer.adapter.persistence.webclient.exception.RestException
import com.housetainer.common.log.logger
import com.housetainer.common.utils.CommonUtils.is2xxSuccessful
import com.housetainer.common.utils.CommonUtils.is5xxServerError
import org.springframework.util.StopWatch
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFunction
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono
import reactor.netty.http.client.PrematureCloseException

object WebClientFilter {

    private val log = logger()

    fun is5xxServerError(throwable: Throwable): Boolean {
        return throwable is PrematureCloseException ||
            (throwable is RestException && throwable.httpStatus.is5xxServerError) ||
            (throwable is WebClientResponseException && throwable.rawStatusCode.is5xxServerError())
    }

    private fun loggingRequest(
        request: ClientRequest,
        next: ExchangeFunction,
        always: Boolean = true
    ): Mono<ClientResponse> {
        val stopWatch = StopWatch().apply { start() }
        return next.exchange(request)
            .flatMap { response ->
                stopWatch.stop()
                if (!response.rawStatusCode().is2xxSuccessful() || always) {
                    log.info(
                        "client.request, {}|{}|{}|{}",
                        request.method(),
                        request.url(),
                        response.rawStatusCode(),
                        stopWatch.totalTimeMillis
                    )
                }
                Mono.just(response)
            }
    }

    fun loggingErrorResult(always: Boolean = false): (ClientRequest, ExchangeFunction) -> Mono<ClientResponse> =
        { request, next ->
            loggingRequest(request, next, always)
        }
}
