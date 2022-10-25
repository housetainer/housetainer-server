package com.housetainer.adapter.persistence.integration.repository


import com.housetainer.adapter.persistence.exception.PersistenceExceptions
import com.housetainer.adapter.persistence.repository.DeviceRepository
import com.housetainer.adapter.persistence.repository.UserRepository
import com.housetainer.common.CoroutineTestUtils
import com.housetainer.domain.entity.auth.AuthProvider
import com.housetainer.domain.entity.device.Device
import com.housetainer.domain.entity.exception.BaseException
import com.housetainer.domain.entity.user.User
import com.housetainer.domain.entity.user.UserStatus
import com.housetainer.domain.entity.user.UserType
import com.housetainer.domain.model.device.UpsertDeviceRequest
import com.housetainer.domain.model.user.CreateUserRequest
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class DeviceRepositorySpec extends RepositorySpecification {

    @Shared
    User user

    @Shared
    Device device

    @Autowired
    DeviceRepository deviceRepository

    @Autowired
    UserRepository userRepository

    def "create a device"() {
        given:
        user = CoroutineTestUtils.executeSuspendFun {
            userRepository.createUser(createUserRequest, it)
        } as User

        def createDeviceRequest = new UpsertDeviceRequest(
            uuid,
            user.userId,
            "Android",
            "12",
            "1.0.0",
            "ko"
        )

        when:
        def result = CoroutineTestUtils.executeSuspendFun {
            deviceRepository.createDevice(createDeviceRequest, it)
        } as Device

        then:
        result != null
        0 * _

        cleanup:
        device = result
    }

    def "get a device"() {
        when:
        def result = CoroutineTestUtils.executeSuspendFun {
            deviceRepository.getDeviceById(device.deviceId, it)
        } as Device

        then:
        result == device
        0 * _
    }

    def "update a device"() {
        given:
        def updateDeviceRequest = new UpsertDeviceRequest(device.deviceId, user.userId).with(true) {
            appVersion = "1.0.1"
        } as UpsertDeviceRequest

        when:
        def result = CoroutineTestUtils.executeSuspendFun {
            deviceRepository.updateDevice(updateDeviceRequest, it)
        } as Device

        then:
        result != null
        result.deviceId == updateDeviceRequest.deviceId
        result.platform == device.platform
        result.appVersion == updateDeviceRequest.appVersion
        0 * _

        cleanup:
        device = result
    }

    def "create device but user not exist"() {
        given:
        def createDeviceRequest = new UpsertDeviceRequest(
            uuid,
            uuid,
            "Android",
            "12",
            "1.0.0",
            "ko"
        )

        when:
        CoroutineTestUtils.executeSuspendFun {
            deviceRepository.createDevice(createDeviceRequest, it)
        }

        then:
        def exception = thrown(BaseException)
        exception == PersistenceExceptions.forbiddenException()
        0 * _
    }

    def getCreateUserRequest() {
        def now = new Date()
        new CreateUserRequest(
            "$uuid@test.com",
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
    }
}
