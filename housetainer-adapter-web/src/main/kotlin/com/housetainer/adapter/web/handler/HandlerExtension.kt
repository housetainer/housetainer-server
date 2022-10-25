package com.housetainer.adapter.web.handler

import com.housetainer.adapter.web.exception.HandlerExceptions
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBodyOrNull
import org.springframework.web.reactive.function.server.buildAndAwait
import org.springframework.web.reactive.function.server.json
import org.springframework.web.util.UriComponentsBuilder

object HandlerExtension {

    suspend fun ok(body: Any): ServerResponse = ServerResponse.ok().json().bodyValue(body).awaitSingle()

    suspend fun created(uri: String, body: Any): ServerResponse = ServerResponse
        .created(UriComponentsBuilder.fromPath(uri).build().toUri())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(body)
        .awaitSingle()

    suspend fun noContent(): ServerResponse = ServerResponse.noContent().buildAndAwait()

    suspend inline fun <reified T : Any> ServerRequest.awaitBodyOrException(exception: Exception): T {
        return this.awaitBodyOrNull<T>() ?: throw exception
    }

    suspend inline fun <reified T : Any> ServerRequest.awaitBodyOrEmptyBodyException(): T {
        return awaitBodyOrException(HandlerExceptions.emptyBodyException())
    }
}
