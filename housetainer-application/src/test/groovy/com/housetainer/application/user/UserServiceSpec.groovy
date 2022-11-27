package com.housetainer.application.user

import com.housetainer.application.ApplicationSpecification
import com.housetainer.domain.entity.exception.BaseException
import com.housetainer.domain.entity.user.User
import com.housetainer.domain.entity.user.UserType
import com.housetainer.domain.model.supporter.UpdateUserRequestBuilder
import com.housetainer.domain.model.supporter.UserBuilder
import com.housetainer.domain.model.user.CreateUserRequest
import com.housetainer.domain.model.user.InternalUpdateUserRequest
import com.housetainer.domain.model.user.UserResponse
import com.housetainer.domain.persistence.user.CreateUserCommand
import com.housetainer.domain.persistence.user.GetUserByIdQuery
import com.housetainer.domain.persistence.user.GetUserByNicknameQuery
import com.housetainer.domain.persistence.user.UpdateUserCommand

class UserServiceSpec extends ApplicationSpecification {

    CreateUserCommand createUserCommand = Mock()
    GetUserByIdQuery getUserByIdQuery = Mock()
    GetUserByNicknameQuery getUserByNicknameQuery = Mock()
    UpdateUserCommand updateUserCommand = Mock()

    UserService service

    def setup() {
        service = new UserService(
            createUserCommand,
            getUserByIdQuery,
            getUserByNicknameQuery,
            updateUserCommand
        )
    }

    def "create user"() {
        given:
        def user = createUser()
        def request = new CreateUserRequest(
            user.email,
            user.authId,
            user.authProvider,
            user.name,
            user.nickname,
            user.gender,
            user.birthday,
            user.phoneNumber,
            user.profileImage,
            user.countryCode,
            user.languageCode,
            user.type,
            user.status,
            user.createTime,
            user.updateTime
        )

        when:
        def result = service.createUser(request, coroutineContext) as User

        then:
        result == user
        1 * createUserCommand.createUser(request, _) >> user
        0 * _
    }

    def "update user"() {
        given:
        def user = createUser()
        def request = UpdateUserRequestBuilder.create(user.userId)
            .nickname("nickname-${uuid.substring(0, 5)}")
            .gender(user.gender)
            .type(UserType.HOUSETAINER)
            .toInternalUpdateUserRequest()
        def updatedUser = UserBuilder.create(user)
            .nickname(request.nickname)
            .gender(request.gender)
            .type(request.type)
            .toUser()

        when:
        def result = service.updateUser(request, coroutineContext) as UserResponse

        then:
        result.userId == user.userId
        result.name == user.name
        result.nickname == request.nickname
        result.type == request.type
        1 * getUserByIdQuery.getUserById(user.userId, _) >> user
        1 * getUserByNicknameQuery.getUserByNickname(*_) >> null
        1 * updateUserCommand.updateUser({ InternalUpdateUserRequest it ->
            it.nickname == request.nickname
            it.type == request.type
            it.gender == null
        }, _) >> updatedUser
        0 * _
    }

    def "update user but nickname is duplicated"() {
        given:
        def user = createUser()
        def newNickname = "nickname-${uuid.substring(0, 5)}"
        def request = UpdateUserRequestBuilder.create(user.userId)
            .nickname(newNickname)
            .gender(user.gender)
            .type(UserType.HOUSETAINER)
            .toInternalUpdateUserRequest()

        when:
        service.updateUser(request, coroutineContext)

        then:
        def exception = thrown(BaseException)
        exception == UserService.nicknameConflictException()
        1 * getUserByIdQuery.getUserById(user.userId, _) >> user
        1 * getUserByNicknameQuery.getUserByNickname(newNickname, _) >> createUser()
        0 * _
    }

    def "update user but not updated"() {
        given:
        def user = createUser()
        def request = UpdateUserRequestBuilder.create(user.userId)
            .nickname(user.nickname)
            .gender(user.gender)
            .type(user.type)
            .toInternalUpdateUserRequest()

        when:
        def result = service.updateUser(request, coroutineContext) as UserResponse

        then:
        result.userId == user.userId
        result.name == user.name
        result.nickname == request.nickname
        result.type == request.type
        1 * getUserByIdQuery.getUserById(user.userId, _) >> user
        0 * updateUserCommand.updateUser(*_)
        0 * _
    }

    def "update user but not found"() {
        given:
        def user = createUser()
        def request = UpdateUserRequestBuilder.create(user.userId)
            .nickname(user.nickname)
            .gender(user.gender)
            .type(user.type)
            .toInternalUpdateUserRequest()

        when:
        service.updateUser(request, coroutineContext)

        then:
        def exception = thrown(BaseException)
        exception == UserService.userNotFoundException()
        1 * getUserByIdQuery.getUserById(user.userId, _) >> null
        0 * _
    }
}
