package com.housetainer.adapter.web.handler

import com.housetainer.adapter.web.handler.HandlerExtension.awaitBodyOrEmptyBodyException
import com.housetainer.adapter.web.handler.HandlerExtension.ok
import com.housetainer.common.log.logger
import com.housetainer.domain.model.user.UpdateUserRequest
import com.housetainer.domain.usecase.user.UpdateUserUseCase
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse

@Component
class UsersHandler(
    private val updateUserUseCase: UpdateUserUseCase
) {

    private val log = logger()

    suspend fun updateUser(request: ServerRequest): ServerResponse {
        val updateUserRequest: UpdateUserRequest = request.awaitBodyOrEmptyBodyException()
        log.info("update-user, request={}", updateUserRequest)

        val userResponse = updateUserUseCase.updateUser(updateUserRequest)

        return ok(userResponse)
    }
}
