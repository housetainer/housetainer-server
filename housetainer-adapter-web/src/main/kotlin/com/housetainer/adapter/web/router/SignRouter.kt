package com.housetainer.adapter.web.router

import com.housetainer.adapter.web.handler.SignHandler
import com.housetainer.common.utils.Constants
import com.housetainer.domain.entity.user.User
import com.housetainer.domain.model.auth.RenewTokenRequest
import com.housetainer.domain.model.auth.SignUpRequest
import com.housetainer.domain.model.user.UserResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.headers.Header
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
                            headers = [Header(name = Constants.HOUSETAINER_TOKEN_HEADER, required = true)],
                            content = [Content(schema = Schema(implementation = UserResponse::class))]
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
                headers = [
                    "Authorization"
                ],
                operation = Operation(
                    operationId = "signIn",
                    summary = "User sign in",
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
            ),
            RouterOperation(
                path = "/sign/token/renew",
                method = [RequestMethod.POST],
                operation = Operation(
                    operationId = "renewToken",
                    summary = "User token re-issue",
                    requestBody = RequestBody(
                        content = [
                            Content(schema = Schema(implementation = RenewTokenRequest::class))
                        ]
                    ),
                    responses = [
                        ApiResponse(
                            responseCode = "204",
                            headers = [Header(name = Constants.HOUSETAINER_TOKEN_HEADER, required = true)]
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
            )
        ]
    )
    fun route(signHandler: SignHandler) = coRouter {
        "/sign".nest {
            POST("/up", signHandler::signUp)
            POST("/in", signHandler::signIn)
            POST("/token/renew", signHandler::renewToken)
        }
    }
}
