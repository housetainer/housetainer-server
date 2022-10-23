package com.housetainer.application.auth

import com.housetainer.application.ApplicationSpecification
import com.housetainer.domain.entity.auth.AuthProvider
import com.housetainer.domain.entity.exception.BaseException
import com.housetainer.domain.entity.user.User
import com.housetainer.domain.entity.user.UserStatus
import com.housetainer.domain.entity.user.UserType
import com.housetainer.domain.model.auth.SignInRequest
import com.housetainer.domain.model.auth.SignUpRequest
import com.housetainer.domain.model.auth.UserProfileResponse
import com.housetainer.domain.model.device.UpsertDeviceRequest
import com.housetainer.domain.model.user.CreateUserRequest
import com.housetainer.domain.persistence.auth.GetUserProfileFromNaverQuery
import com.housetainer.domain.persistence.user.GetUserByEmailQuery
import com.housetainer.domain.usecase.device.UpsertDeviceUseCase
import com.housetainer.domain.usecase.user.CreateUserUseCase

class SignServiceSpec extends ApplicationSpecification {

    GetUserProfileFromNaverQuery getUserProfileFromNaverQuery = Mock()
    GetUserByEmailQuery getUserByEmailQuery = Mock()
    CreateUserUseCase createUserUseCase = Mock()
    UpsertDeviceUseCase upsertDeviceUseCase = Mock()

    SignService service

    def setup() {
        service = new SignService(
            getUserProfileFromNaverQuery,
            getUserByEmailQuery,
            createUserUseCase,
            upsertDeviceUseCase
        )
    }

    def "sign up"() {
        given:
        def accessToken = uuid
        def authId = uuid
        def deviceId = uuid
        def request = new SignUpRequest(
            accessToken,
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
            deviceId,
            "Android",
            "12",
            "1.0",
            "ko_kr"
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
        def userProfileResponse = new UserProfileResponse(
            request.authId,
            request.email,
            request.name,
            request.nickname,
            request.gender,
            request.birthday,
            request.profileImage,
            request.phoneNumber
        )

        when:
        def result = service.signUp(request, coroutineContext) as User

        then:
        result == user
        1 * getUserProfileFromNaverQuery.getUserProfile(accessToken, _) >> userProfileResponse
        1 * getUserByEmailQuery.getUserByEmail(request.email, _) >> null
        1 * createUserUseCase.createUser({ CreateUserRequest it ->
            it.createTime > 0
            it.updateTime > 0
            it.createTime == it.updateTime
            it.type == UserType.MEMBER
            it.status == UserStatus.ACTIVE
        }, _) >> user
        1 * upsertDeviceUseCase.upsertDevice({ UpsertDeviceRequest it ->
            it.deviceId == request.deviceId
            it.userId == user.userId
            it.platform == request.platform
            it.platformVersion == request.platformVersion
            it.appVersion == request.appVersion
            it.locale == request.locale
        }, _)
        0 * _
    }

    def "sign up but invalid token"() {
        given:
        def accessToken = uuid
        def authId = uuid
        def deviceId = uuid
        def request = new SignUpRequest(
            accessToken,
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
            deviceId,
            "Android",
            "12",
            "1.0",
            "ko_kr"
        )
        def userProfileResponse = new UserProfileResponse(
            uuid,
            request.email,
            request.name,
            request.nickname,
            request.gender,
            request.birthday,
            request.profileImage,
            request.phoneNumber
        )

        when:
        service.signUp(request, coroutineContext) as User

        then:
        def exception = thrown(BaseException)
        exception == SignService.userUnauthorizedException()
        1 * getUserProfileFromNaverQuery.getUserProfile(accessToken, _) >> userProfileResponse
        0 * _
    }

    def "sign up but duplicated"() {
        given:
        def accessToken = uuid
        def authId = uuid
        def deviceId = uuid
        def request = new SignUpRequest(
            accessToken,
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
            deviceId,
            "Android",
            "12",
            "1.0",
            "ko_kr"
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
        def userProfileResponse = new UserProfileResponse(
            request.authId,
            request.email,
            request.name,
            request.nickname,
            request.gender,
            request.birthday,
            request.profileImage,
            request.phoneNumber
        )

        when:
        def result = service.signUp(request, coroutineContext) as User

        then:
        result == user
        1 * getUserProfileFromNaverQuery.getUserProfile(accessToken, _) >> userProfileResponse
        1 * getUserByEmailQuery.getUserByEmail(request.email, _) >> user
        1 * upsertDeviceUseCase.upsertDevice({ UpsertDeviceRequest it ->
            it.deviceId == request.deviceId
            it.userId == user.userId
            it.platform == request.platform
            it.platformVersion == request.platformVersion
            it.appVersion == request.appVersion
            it.locale == request.locale
        }, _)
        0 * _
    }

    def "sign in"() {
        def accessToken = uuid
        def authId = uuid
        def userId = uuid
        def request = new SignInRequest(
            accessToken,
            AuthProvider.NAVER,
            userId
        )
        def user = new User(
            uuid,
            "test@test.com",
            authId,
            request.authProvider,
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
        def userProfileResponse = new UserProfileResponse(
            user.authId,
            user.email,
            user.name,
            user.nickname,
            user.gender,
            user.birthday,
            user.profileImage,
            user.phoneNumber
        )

        when:
        def result = service.signIn(request, coroutineContext) as User

        then:
        result == user
        1 * getUserProfileFromNaverQuery.getUserProfile(accessToken, _) >> userProfileResponse
        1 * getUserByEmailQuery.getUserByEmail(user.email, _) >> user
        0 * _
    }

    def "sign in but user not found"() {
        def accessToken = uuid
        def userId = uuid
        def request = new SignInRequest(
            accessToken,
            AuthProvider.NAVER,
            userId
        )
        def userProfileResponse = new UserProfileResponse(
            uuid,
            "test@test.com",
            "name",
            null,
            null,
            null,
            null,
            null
        )

        when:
        service.signIn(request, coroutineContext) as User

        then:
        def exception = thrown(BaseException)
        exception == SignService.userNotFoundException()
        1 * getUserProfileFromNaverQuery.getUserProfile(accessToken, _) >> userProfileResponse
        1 * getUserByEmailQuery.getUserByEmail("test@test.com", _) >> null
        0 * _
    }
}
