package com.housetainer.application

import com.housetainer.common.BaseSpecification
import com.housetainer.domain.entity.auth.AuthProvider
import com.housetainer.domain.entity.user.User
import com.housetainer.domain.entity.user.UserStatus
import com.housetainer.domain.entity.user.UserType
import com.housetainer.domain.port.token.TokenService
import spock.lang.Shared

import java.time.Duration

class ApplicationSpecification extends BaseSpecification {

    @Shared
    String secretKey = uuid

    @Shared
    Duration timeout = Duration.ofMinutes(1)

    @Shared
    TokenService tokenService = TokenService.INSTANCE.getInstance(secretKey, timeout)

    def createUser(String userId = uuid) {
        new User(
            userId,
            "test@test.com",
            uuid,
            AuthProvider.NAVER,
            "name",
            "nickname",
            "M",
            "2020-01-01",
            "phoneNumber",
            "profileImage",
            "countryCode",
            "languageCode",
            UserType.MEMBER,
            UserStatus.ACTIVE,
            System.currentTimeMillis(),
            System.currentTimeMillis()
        )
    }

}
