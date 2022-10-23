package com.housetainer.application

import com.housetainer.common.BaseSpecification
import com.housetainer.domain.entity.auth.AuthProvider
import com.housetainer.domain.entity.user.User
import com.housetainer.domain.entity.user.UserStatus
import com.housetainer.domain.entity.user.UserType

class ApplicationSpecification extends BaseSpecification {

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
