package com.housetainer.adapter.persistence.integration.repository

import com.housetainer.adapter.persistence.repository.UserRepository
import com.housetainer.common.CoroutineTestUtils
import com.housetainer.domain.entity.auth.AuthProvider
import com.housetainer.domain.entity.user.User
import com.housetainer.domain.entity.user.UserStatus
import com.housetainer.domain.entity.user.UserType
import com.housetainer.domain.model.user.CreateUserRequest
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class UserRepositorySpec extends RepositorySpecification {

    @Shared
    User user

    @Autowired
    UserRepository userRepository

    def "create user"() {
        given:
        def now = new Date()
        def request = new CreateUserRequest(
            "test@test.com",
            uuid,
            AuthProvider.NAVER,
            "name",
            "nickname",
            "F",
            "2000-01-01",
            null,
            "http://profile-image.jpg",
            "KR",
            "ko",
            UserType.MEMBER,
            UserStatus.ACTIVE,
            now.toInstant().toEpochMilli(),
            now.toInstant().toEpochMilli()
        )

        when:
        def result = CoroutineTestUtils.executeSuspendFun {
            userRepository.createUser(request, it)
        } as User

        then:
        result.userId != null
        result.email == request.email
        result.authId == request.authId
        result.authProvider == request.authProvider
        result.name == request.name
        result.nickname == request.nickname
        result.gender == request.gender
        result.birthday == request.birthday
        result.phoneNumber == request.phoneNumber
        result.profileImage == request.profileImage
        result.countryCode == request.countryCode
        result.languageCode == request.languageCode
        result.type == request.type
        result.status == request.status
        result.createTime == request.createTime
        result.updateTime == request.updateTime
        0 * _

        cleanup:
        user = result
    }

    def "get user by userId"() {
        when:
        def result = CoroutineTestUtils.executeSuspendFun {
            userRepository.getUserById(user.userId, it)
        } as User

        then:
        result == user
        0 * _
    }

}
