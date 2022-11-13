package com.housetainer.configuration.http

import com.housetainer.common.log.logger
import com.housetainer.common.utils.CommonUtils.extractCausedBy
import com.housetainer.common.utils.CommonUtils.is5xxServerError
import com.housetainer.configuration.filter.RequestLogger
import com.housetainer.model.ErrorResponse
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
@Order(-2)
class ServerExceptionHandler(
    g: ServerErrorAttributes?,
    applicationContext: ApplicationContext?,
    serverCodecConfigurer: ServerCodecConfigurer
) : AbstractErrorWebExceptionHandler(g, WebProperties.Resources(), applicationContext) {

    private val log = logger()

    init {
        super.setMessageWriters(serverCodecConfigurer.writers)
        super.setMessageReaders(serverCodecConfigurer.readers)
    }

    override fun getRoutingFunction(errorAttributes: ErrorAttributes): RouterFunction<ServerResponse> {
        return RouterFunctions.route(RequestPredicates.all()) { renderErrorResponse(it) }
    }

    override fun logError(request: ServerRequest, response: ServerResponse, throwable: Throwable) {
        RequestLogger.logging(request, response, throwable)
        if (response.rawStatusCode().is5xxServerError()) {
            log.error(
                "{}|{}|{}|{}|",
                request.method(),
                request.uri().path,
                response.rawStatusCode(),
                throwable.extractCausedBy()
            )
        }
    }

    private fun renderErrorResponse(request: ServerRequest): Mono<ServerResponse> {
        val errorPropertiesMap = getErrorAttributes(request, ErrorAttributeOptions.defaults())
        val status = errorPropertiesMap.remove("status") as HttpStatus
        val errorResponse = errorPropertiesMap.remove("error") as ErrorResponse

        return ServerResponse.status(status)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(errorResponse)
    }
}
