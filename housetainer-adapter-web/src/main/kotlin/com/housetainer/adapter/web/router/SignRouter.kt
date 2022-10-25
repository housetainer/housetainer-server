package com.housetainer.adapter.web.router

import com.housetainer.adapter.web.handler.SignHandler
import com.housetainer.domain.entity.user.User
import com.housetainer.domain.model.auth.SignInRequest
import com.housetainer.domain.model.auth.SignUpRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class SignRouter {

    @Bean
    @RouterOperations(
        value = [
            RouterOperation(
                path = "/sign/up",
                method = [RequestMethod.POST],
                operation = Operation(
                    operationId = "signUp",
                    summary = "User sign up",
                    requestBody = RequestBody(
                        content = [
                            Content(schema = Schema(implementation = SignUpRequest::class))
                        ]
                    ),
                    responses = [
                        ApiResponse(
                            responseCode = "200",
                            content = [Content(schema = Schema(implementation = User::class))]
                        ),
                        ApiResponse(
                            responseCode = "400"
                        ),
                        ApiResponse(
                            responseCode = "401"
                        ),
                        ApiResponse(
                            responseCode = "403"
                        ),
                    ]
                )
            ),
            RouterOperation(
                path = "/sign/in",
                method = [RequestMethod.POST],
                operation = Operation(
                    operationId = "signIn",
                    summary = "User sign in",
                    requestBody = RequestBody(
                        content = [Content(schema = Schema(implementation = SignInRequest::class))]
                    ),
                    responses = [
                        ApiResponse(
                            responseCode = "200",
                            content = [Content(schema = Schema(implementation = User::class))]
                        ),
                        ApiResponse(
                            responseCode = "400"
                        ),
                        ApiResponse(
                            responseCode = "401"
                        ),
                        ApiResponse(
                            responseCode = "404"
                        )
                    ]
                )
            )
        ]
    )
    fun route(signHandler: SignHandler) = coRouter {
        "/sign".nest {
            POST("/up", signHandler::signUp)
            POST("/in", signHandler::signIn)
        }
    }
}
