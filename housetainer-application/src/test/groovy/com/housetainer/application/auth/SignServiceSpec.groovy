package com.housetainer.application.auth

import com.housetainer.application.ApplicationSpecification
import com.housetainer.domain.entity.auth.AuthProvider
import com.housetainer.domain.entity.exception.BaseException
import com.housetainer.domain.entity.user.User
import com.housetainer.domain.entity.user.UserStatus
import com.housetainer.domain.entity.user.UserType
import com.housetainer.domain.model.auth.InternalIssueTokenRequest
import com.housetainer.domain.model.auth.InternalSignUpResult
import com.housetainer.domain.model.auth.RenewTokenRequest
import com.housetainer.domain.model.auth.SignUpRequest
import com.housetainer.domain.model.user.CreateUserRequest
import com.housetainer.domain.model.user.UserResponse
import com.housetainer.domain.persistence.user.GetUserByEmailQuery
import com.housetainer.domain.persistence.user.GetUserByIdQuery
import com.housetainer.domain.port.token.TokenService
import com.housetainer.domain.usecase.user.CreateUserUseCase
import spock.lang.Shared
import spock.lang.Unroll

import java.time.Duration

class SignServiceSpec extends ApplicationSpecification {

    @Shared
    String secretKey = uuid

    @Shared
    Duration timeout = Duration.ofSeconds(3)

    GetUserByIdQuery getUserByIdQuery = Mock()
    GetUserByEmailQuery getUserByEmailQuery = Mock()
    CreateUserUseCase createUserUseCase = Mock()

    SignService sut

    def setup() {
        sut = new SignService(
            getUserByIdQuery,
            getUserByEmailQuery,
            createUserUseCase,
            secretKey,
            timeout
        )
    }

    def "sign up"() {
        given:
        def authId = uuid
        def request = new SignUpRequest(
            "test@test.com",
            authId,
            AuthProvider.NAVER,
            "name",
            "nickname",
            "M",
            "2020-01-01",
            "010-0000-0000",
            null,
            null,
            null
        )
        def user = new User(
            uuid,
            request.email,
            request.authId,
            request.authProvider,
            request.name,
            request.nickname,
            request.gender,
            request.birthday,
            request.phoneNumber,
            request.profileImage,
            request.countryCode,
            request.languageCode,
            UserType.MEMBER,
            UserStatus.ACTIVE,
            System.currentTimeMillis(),
            System.currentTimeMillis()
        )

        when:
        def result = sut.signUp(request, coroutineContext) as InternalSignUpResult

        then:
        compareUser(user, result.user)
        result.token != null
        1 * getUserByEmailQuery.getUserByEmail(request.email, _) >> null
        1 * createUserUseCase.createUser({ CreateUserRequest it ->
            it.createTime > 0
            it.updateTime > 0
            it.createTime == it.updateTime
            it.type == UserType.MEMBER
            it.status == UserStatus.ACTIVE
        }, _) >> user
        0 * _
    }

    def "sign up but duplicated"() {
        given:
        def authId = uuid
        def request = new SignUpRequest(
            "test@test.com",
            authId,
            AuthProvider.NAVER,
            "name",
            "nickname",
            "M",
            "2020-01-01",
            "010-0000-0000",
            null,
            null,
            null
        )
        def user = new User(
            uuid,
            request.email,
            request.authId,
            request.authProvider,
            request.name,
            request.nickname,
            request.gender,
            request.birthday,
            request.phoneNumber,
            request.profileImage,
            request.countryCode,
            request.languageCode,
            UserType.MEMBER,
            UserStatus.ACTIVE,
            System.currentTimeMillis(),
            System.currentTimeMillis()
        )

        when:
        def result = sut.signUp(request, coroutineContext) as InternalSignUpResult

        then:
        compareUser(user, result.user)
        1 * getUserByEmailQuery.getUserByEmail(request.email, _) >> user
        0 * _
    }

    def "sign in"() {
        given:
        def authId = uuid
        def userId = uuid
        def token = TokenService.INSTANCE.issueToken(new InternalIssueTokenRequest(
            userId, authId, AuthProvider.NAVER
        ))
        def user = new User(
            userId,
            "test@test.com",
            authId,
            AuthProvider.NAVER,
            "name",
            "nickname",
            "M",
            "2020-01-01",
            "010-0000-0000",
            null,
            null,
            null,
            UserType.MEMBER,
            UserStatus.ACTIVE,
            System.currentTimeMillis(),
            System.currentTimeMillis()
        )

        when:
        def result = sut.signIn(token, coroutineContext) as UserResponse

        then:
        compareUser(user, result)
        1 * getUserByIdQuery.getUserById(userId, _) >> user
        0 * _
    }

    def "sign in but user not found"() {
        given:
        def userId = uuid
        def authId = uuid
        def token = TokenService.INSTANCE.issueToken(new InternalIssueTokenRequest(
            userId, authId, AuthProvider.NAVER
        ))

        when:
        sut.signIn(token, coroutineContext)

        then:
        def exception = thrown(BaseException)
        exception == SignService.userNotFoundException()
        1 * getUserByIdQuery.getUserById(userId, _) >> null
        0 * _
    }

    @Unroll
    def "renew token"() {
        given:
        def user = createUser()
        def request = new RenewTokenRequest(
            email,
            userId,
            uuid,
            AuthProvider.NAVER
        )

        when:
        def result = sut.renewToken(request, coroutineContext) as String

        then:
        result != null
        (email == null ? 0 : 1) * getUserByEmailQuery.getUserByEmail(email, _) >> user
        (userId == null ? 0 : 1) * getUserByIdQuery.getUserById(userId, _) >> user
        0 * _

        where:
        email           | userId
        null            | uuid
        "test@test.com" | null
    }

    def "renew token but userId and email are empty"() {
        given:
        def request = new RenewTokenRequest(
            null,
            null,
            uuid,
            AuthProvider.NAVER
        )

        when:
        sut.renewToken(request, coroutineContext)

        then:
        def exception = thrown(BaseException)
        exception.message == SignService.userIdOrEmailRequiredException().message
        0 * _
    }

    def "renew token but user not found"() {
        given:
        def request = new RenewTokenRequest(
            null,
            uuid,
            uuid,
            AuthProvider.NAVER
        )

        when:
        sut.renewToken(request, coroutineContext)

        then:
        def exception = thrown(BaseException)
        exception.message == SignService.userNotFoundException().message
        1 * getUserByIdQuery.getUserById(request.userId, _) >> null
        0 * _
    }

    def compareUser(User user, UserResponse userResponse) {
        return user.userId == userResponse.userId &&
            user.email == userResponse.email &&
            user.authId == userResponse.authId &&
            user.authProvider == userResponse.authProvider &&
            user.name == userResponse.name &&
            user.nickname == userResponse.nickname &&
            user.gender == userResponse.gender &&
            user.birthday == userResponse.birthday &&
            user.phoneNumber == userResponse.phoneNumber &&
            user.profileImage == userResponse.profileImage &&
            user.countryCode == userResponse.countryCode &&
            user.languageCode == userResponse.languageCode &&
            user.type == userResponse.type &&
            user.status == userResponse.status &&
            user.createTime == userResponse.createTime &&
            user.updateTime == userResponse.updateTime
    }
}
