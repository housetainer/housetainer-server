package com.housetainer.domain.model.supporter

import com.housetainer.domain.entity.user.UserStatus
import com.housetainer.domain.entity.user.UserType
import com.housetainer.domain.model.user.InternalUpdateUserRequest
import com.housetainer.domain.model.user.UpdateUserRequest
import groovy.transform.builder.Builder
import groovy.transform.builder.ExternalStrategy

@Builder(builderStrategy = ExternalStrategy, forClass = BuilderUpdateUserRequest)
class UpdateUserRequestBuilder {

    private UpdateUserRequestBuilder() {}

    public static def create() {
        new UpdateUserRequestBuilder()
    }

    public static def create(String userId) {
        new UpdateUserRequestBuilder().userId(userId)
    }

    UpdateUserRequest toUpdateUserRequest() {
        def internalRequest = build()
        new UpdateUserRequest(
            internalRequest.nickname,
            internalRequest.gender,
            internalRequest.birthday,
            internalRequest.phoneNumber,
            internalRequest.profileImage,
            internalRequest.countryCode,
            internalRequest.languageCode,
            internalRequest.type,
            internalRequest.status
        )
    }

    InternalUpdateUserRequest toInternalUpdateUserRequest() {
        def internalRequest = build()
        new InternalUpdateUserRequest(
            internalRequest.userId,
            internalRequest.nickname,
            internalRequest.gender,
            internalRequest.birthday,
            internalRequest.phoneNumber,
            internalRequest.profileImage,
            internalRequest.countryCode,
            internalRequest.languageCode,
            internalRequest.type,
            internalRequest.status
        )
    }

    private class BuilderUpdateUserRequest {
        String userId
        String nickname
        String gender
        String birthday
        String phoneNumber
        String profileImage
        String countryCode
        String languageCode
        UserType type
        UserStatus status
    }
}
