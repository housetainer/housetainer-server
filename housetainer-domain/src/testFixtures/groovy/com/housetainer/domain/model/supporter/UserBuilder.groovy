package com.housetainer.domain.model.supporter

import com.housetainer.domain.entity.auth.AuthProvider
import com.housetainer.domain.entity.user.User
import com.housetainer.domain.entity.user.UserStatus
import com.housetainer.domain.entity.user.UserType
import com.housetainer.domain.model.user.UserResponse
import groovy.transform.builder.Builder
import groovy.transform.builder.ExternalStrategy

@Builder(builderStrategy = ExternalStrategy, forClass = InternalUser)
class UserBuilder {

    private UserBuilder() {}

    static def create(
        String userId,
        String email,
        String authId,
        AuthProvider authProvider,
        String name,
        UserStatus status
    ) {
        new UserBuilder()
            .userId(userId)
            .email(email)
            .authId(authId)
            .authProvider(authProvider)
            .name(name)
            .status(status)
    }

    static def create(User user) {
        create(user.userId, user.email, user.authId, user.authProvider, user.name, user.status)
            .nickname(user.nickname)
            .gender(user.gender)
            .birthday(user.birthday)
            .phoneNumber(user.phoneNumber)
            .profileImage(user.profileImage)
            .countryCode(user.countryCode)
            .languageCode(user.languageCode)
            .type(user.type)
            .createTime(user.createTime)
            .updateTime(user.updateTime)
    }

    User toUser() {
        def internalUser = build()
        new User(
            internalUser.userId,
            internalUser.email,
            internalUser.authId,
            internalUser.authProvider,
            internalUser.name,
            internalUser.nickname,
            internalUser.gender,
            internalUser.birthday,
            internalUser.phoneNumber,
            internalUser.profileImage,
            internalUser.countryCode,
            internalUser.languageCode,
            internalUser.type,
            internalUser.status,
            internalUser.createTime,
            internalUser.updateTime
        )
    }

    UserResponse toUserResponse() {
        def internalUser = build()
        new UserResponse(
            internalUser.userId,
            internalUser.email,
            internalUser.authId,
            internalUser.authProvider,
            internalUser.name,
            internalUser.nickname,
            internalUser.gender,
            internalUser.birthday,
            internalUser.phoneNumber,
            internalUser.profileImage,
            internalUser.countryCode,
            internalUser.languageCode,
            internalUser.type,
            internalUser.status,
            0L,
            0L
        )
    }

    private class InternalUser {
        String userId
        String email
        String authId
        AuthProvider authProvider
        String name
        String nickname
        String gender
        String birthday
        String phoneNumber
        String profileImage
        String countryCode
        String languageCode
        UserType type
        UserStatus status
        Long createTime = 0L
        Long updateTime = 0L
    }
}
