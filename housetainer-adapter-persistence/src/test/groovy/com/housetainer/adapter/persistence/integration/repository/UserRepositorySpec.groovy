package com.housetainer.adapter.persistence.integration.repository

import com.housetainer.adapter.persistence.integration.PersistenceModuleSpecification
import com.housetainer.adapter.persistence.repository.UserRepository
import com.housetainer.common.CoroutineTestUtils
import com.housetainer.domain.entity.auth.AuthProvider
import com.housetainer.domain.entity.user.User
import com.housetainer.domain.entity.user.UserStatus
import com.housetainer.domain.entity.user.UserType
import com.housetainer.domain.model.supporter.UpdateUserRequestBuilder
import com.housetainer.domain.model.user.CreateUserRequest
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class UserRepositorySpec extends PersistenceModuleSpecification {

    @Shared
    User user

    @Autowired
    UserRepository userRepository

    def "create user"() {
        given:
        def now = new Date()
        def request = new CreateUserRequest(
            "$uuid@test.com",
            uuid,
            AuthProvider.NAVER,
            "name",
            "nickname-${uuid.substring(0, 10)}",
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

    def "get user by email"() {
        when:
        def result = CoroutineTestUtils.executeSuspendFun {
            userRepository.getUserByEmail(user.email, it)
        } as User

        then:
        result == user
        0 * _
    }

    def "update user"() {
        given:
        def request = UpdateUserRequestBuilder.create(user.userId)
            .nickname("nickname-${uuid.substring(0, 10)}")
            .toUpdateUserRequest()

        when:
        def result = CoroutineTestUtils.executeSuspendFun {
            userRepository.updateUser(request, it)
        } as User

        then:
        result.userId == user.userId
        result.nickname != user.nickname
        result.nickname == request.nickname
        result.updateTime != user.updateTime
        0 * _

        cleanup:
        user = result
    }
}
