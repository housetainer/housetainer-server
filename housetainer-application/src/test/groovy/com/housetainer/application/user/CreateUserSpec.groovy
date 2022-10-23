package com.housetainer.application.user

import com.housetainer.application.ApplicationSpecification
import com.housetainer.domain.entity.user.User
import com.housetainer.domain.model.user.CreateUserRequest
import com.housetainer.domain.persistence.user.CreateUserCommand

class CreateUserSpec extends ApplicationSpecification {

    CreateUserCommand createUserCommand = Mock()

    UserService service

    def setup() {
        service = new UserService(createUserCommand)
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
}
