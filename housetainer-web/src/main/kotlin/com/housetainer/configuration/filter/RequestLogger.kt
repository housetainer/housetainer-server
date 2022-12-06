package com.housetainer.configuration.filter

import com.housetainer.adapter.web.handler.HandlerExtension
import com.housetainer.common.log.logger
import com.housetainer.common.utils.CommonUtils
import com.housetainer.common.utils.CommonUtils.applyIf
import org.springframework.util.StopWatch
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.util.pattern.PathPattern

object RequestLogger {

    private val OPEN_BRACKET = "\\{".toRegex()
    private val CLOSE_BRACKET = "\\}".toRegex()

    private val log = logger()

    fun logging(exchange: ServerWebExchange, throwable: Throwable? = null) {
        val request: ServerRequest? = exchange.getAttribute<ServerRequest?>(RouterFunctions.REQUEST_ATTRIBUTE)
        val requestInfo = parseRequest(request, exchange.response.statusCode?.value() ?: 0, throwable)
        logging(requestInfo)
    }

    fun logging(request: ServerRequest, response: ServerResponse, throwable: Throwable?) {
        val requestInfo = parseRequest(request, response.rawStatusCode(), throwable)
        logging(requestInfo)
    }

    private fun logging(requestInfo: RequestInformation?) {
        if (requestInfo != null) {
            log.info(
                "{}|{}|{}|{}|{}|{}|{}|{}",
                requestInfo.method,
                requestInfo.path,
                requestInfo.pathVariables,
                requestInfo.queryParams,
                requestInfo.statusCode,
                requestInfo.duration,
                requestInfo.throwableReason ?: "",
                requestInfo.requestBody ?: ""
            )
        }
    }

    private fun parseRequest(request: ServerRequest?, statusCode: Int, throwable: Throwable?): RequestInformation? {
        if (request == null) {
            return null
        }

        val attributes = request.attributes()

        val method = request.methodName()

        val pathMatched = attributes[RouterFunctions.MATCHING_PATTERN_ATTRIBUTE] as PathPattern? ?: return null

        val path = pathMatched.patternString
            .replace(OPEN_BRACKET, RequestInformation.PATH_PREFIX)
            .replace(CLOSE_BRACKET, "")

        val pathVariables = path.split("/")
            .filter { it.startsWith(RequestInformation.PATH_PREFIX) }
            .map { it.replace(RequestInformation.PATH_PREFIX, "") }
            .joinToString(RequestInformation.SEPARATOR) { "$it=${request.pathVariable(it)}" }

        val params = request.queryParams().entries
            .map { (k, v) ->
                val value = if (v.size == 1) {
                    v[0]?.toString()
                } else {
                    v.joinToString(RequestInformation.SEPARATOR, "[", "]")
                }
                k to value
            }
            .joinToString(RequestInformation.SEPARATOR) { (k, v) -> "$k=$v" }

        val duration: Long = (attributes["stopWatch"] as StopWatch?)
            ?.applyIf({ this.isRunning }) { this.stop() }
            ?.totalTimeMillis
            ?: 0

        val requestInfo = RequestInformation(method, path, pathVariables, params, statusCode, duration)
        if (throwable != null) {
            requestInfo.requestBody = attributes[HandlerExtension.REQUEST_BODY]
                ?.let { CommonUtils.mapper.writeValueAsString(it) }
                ?: ""

            requestInfo.throwableReason = throwable.message ?: ""
        }

        return requestInfo
    }
}

data class RequestInformation(
    var method: String,
    var path: String,
    var pathVariables: String,
    var queryParams: String,
    var statusCode: Int,
    var duration: Long,
    var requestBody: String? = null,
    var throwableReason: String? = null
) {
    companion object {
        const val PATH_PREFIX = ":"
        const val SEPARATOR = ","
    }
}
