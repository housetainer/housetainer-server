package com.housetainer.adapter.web.handler

import com.housetainer.adapter.web.handler.HandlerExtension.awaitBodyOrEmptyBodyException
import com.housetainer.adapter.web.handler.HandlerExtension.getTokenInformation
import com.housetainer.adapter.web.handler.HandlerExtension.ok
import com.housetainer.common.log.logger
import com.housetainer.domain.model.user.InternalUpdateUserRequest
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

        val tokenInformation = request.getTokenInformation()

        val userResponse = updateUserUseCase.updateUser(
            InternalUpdateUserRequest(
                userId = tokenInformation.userId,
                nickname = updateUserRequest.nickname,
                gender = updateUserRequest.gender,
                birthday = updateUserRequest.birthday,
                phoneNumber = updateUserRequest.phoneNumber,
                profileImage = updateUserRequest.profileImage,
                countryCode = updateUserRequest.countryCode,
                languageCode = updateUserRequest.languageCode,
                type = updateUserRequest.type,
                status = updateUserRequest.status
            )
        )

        return ok(userResponse)
    }
}
