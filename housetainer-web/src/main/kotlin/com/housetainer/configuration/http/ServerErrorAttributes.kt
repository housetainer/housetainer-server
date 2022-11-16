package com.housetainer.configuration.http

import com.housetainer.common.utils.CurrentContextInfo
import com.housetainer.domain.entity.exception.BaseException
import com.housetainer.model.ErrorResponse
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.server.ResponseStatusException

@Component
class ServerErrorAttributes : DefaultErrorAttributes() {

    override fun getErrorAttributes(request: ServerRequest, options: ErrorAttributeOptions): Map<String, Any> {
        val requestId = request.exchange().getAttribute(CurrentContextInfo.LOGGING_CONTEXT_TRACE_ID) ?: ""

        return when (val throwable = getError(request)) {
            is BaseException -> {
                mapOf(
                    "status" to HttpStatus.valueOf(throwable.code),
                    "error" to buildErrorResponse(requestId, HttpStatus.valueOf(throwable.code), throwable.message)
                )
            }

            is WebClientResponseException -> {
                mapOf(
                    "status" to throwable.statusCode,
                    "error" to buildErrorResponse(requestId, throwable.statusCode, throwable.responseBodyAsString)
                )
            }

            is ResponseStatusException -> {
                mapOf(
                    "status" to throwable.status,
                    "error" to buildErrorResponse(requestId, throwable.status, throwable.message)
                )
            }

            else -> {
                mapOf(
                    "status" to HttpStatus.INTERNAL_SERVER_ERROR,
                    "error" to buildErrorResponse(requestId, HttpStatus.INTERNAL_SERVER_ERROR, null)
                )
            }
        }
    }

    private fun buildErrorResponse(requestId: String, httpStatus: HttpStatus, message: String?): ErrorResponse {
        return ErrorResponse(requestId, httpStatus.value(), message)
    }
}
