package com.housetainer.adapter.web.handler

import com.housetainer.adapter.web.exception.HandlerExceptions
import com.housetainer.adapter.web.router.filter.decorator.RequireUserTokenFilter
import com.housetainer.domain.entity.auth.TokenInformation
import com.housetainer.domain.entity.exception.BaseException
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBodyOrNull
import org.springframework.web.reactive.function.server.buildAndAwait
import org.springframework.web.reactive.function.server.json
import org.springframework.web.reactive.function.server.queryParamOrNull
import org.springframework.web.util.UriComponentsBuilder

object HandlerExtension {

    const val REQUEST_BODY = "RequestBody"

    suspend fun ok(body: Any, headerBlock: (HttpHeaders) -> Unit = {}): ServerResponse = ServerResponse.ok()
        .headers(headerBlock)
        .json()
        .bodyValue(body)
        .awaitSingle()

    suspend fun created(uri: String, body: Any): ServerResponse = ServerResponse
        .created(UriComponentsBuilder.fromPath(uri).build().toUri())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(body)
        .awaitSingle()

    suspend fun accepted(): ServerResponse = ServerResponse.accepted()
        .buildAndAwait()

    suspend fun noContent(headerBlock: (HttpHeaders) -> Unit = {}): ServerResponse = ServerResponse.noContent()
        .headers(headerBlock)
        .buildAndAwait()

    suspend inline fun <reified T : Any> ServerRequest.awaitBodyOrException(exception: Exception): T {
        return this.awaitBodyOrNull<T>()
            ?.also { body ->
                this.attributes()[REQUEST_BODY] = body
            }
            ?: throw exception
    }

    suspend inline fun <reified T : Any> ServerRequest.awaitBodyOrEmptyBodyException(): T {
        return awaitBodyOrException(HandlerExceptions.emptyBodyException())
    }

    fun ServerRequest.queryParamToIntOrNull(name: String): Int? {
        return runCatching {
            this.queryParamOrNull(name)?.toInt()
        }.onFailure {
            throw HandlerExceptions.queryNotValidTypeException()
        }.getOrNull()
    }

    fun ServerRequest.queryParamToBooleanOrNull(name: String): Boolean? {
        return runCatching {
            this.queryParamOrNull(name)?.toBoolean()
        }.onFailure {
            throw HandlerExceptions.queryNotValidTypeException()
        }.getOrNull()
    }

    fun ServerRequest.notEmptyPathVariable(name: String, exception: Exception? = null): String {
        return this.pathVariable(name).takeIf { it.isNotBlank() }
            ?: throw exception ?: BaseException(400, "$name is required")
    }

    fun ServerRequest.getTokenInformation(): TokenInformation {
        return (attributes()[RequireUserTokenFilter.HEADER_TOKEN_INFORMATION] as TokenInformation?)
            ?: throw BaseException(500)
    }
}
