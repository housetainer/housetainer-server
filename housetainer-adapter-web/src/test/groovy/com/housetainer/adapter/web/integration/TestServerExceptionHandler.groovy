package com.housetainer.adapter.web.integration

import com.housetainer.domain.entity.exception.BaseException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Component
class TestServerExceptionHandler extends AbstractErrorWebExceptionHandler {

    @Autowired
    TestServerExceptionHandler(
        ErrorAttributes errorAttributes,
        ApplicationContext applicationContext,
        ServerCodecConfigurer serverCodecConfigurer
    ) {
        super(errorAttributes, new WebProperties.Resources(), applicationContext)
        setMessageReaders(serverCodecConfigurer.readers)
        setMessageWriters(serverCodecConfigurer.writers)
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), {
            def error = errorAttributes.getError(it)
            if (error instanceof BaseException) {
                BaseException baseException = error as BaseException
                return ServerResponse.status(baseException.code)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(baseException)
            } else {
                return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new BaseException(500, error.message))
            }
        })
    }
}
