package com.housetainer.application.device

import com.housetainer.application.ApplicationSpecification
import com.housetainer.domain.entity.device.Device
import com.housetainer.domain.model.device.UpsertDeviceRequest
import com.housetainer.domain.persistence.device.CreateDeviceCommand
import com.housetainer.domain.persistence.device.GetDeviceByIdQuery
import com.housetainer.domain.persistence.device.UpdateDeviceCommand

class DeviceServiceSpec extends ApplicationSpecification {

    GetDeviceByIdQuery getDeviceQuery = Mock()
    CreateDeviceCommand createDeviceCommand = Mock()
    UpdateDeviceCommand updateDeviceCommand = Mock()

    DeviceService service

    def setup() {
        service = new DeviceService(
            getDeviceQuery, createDeviceCommand, updateDeviceCommand
        )
    }

    def "create device"() {
        given:
        def deviceId = uuid
        def user = createUser()
        def request = new UpsertDeviceRequest(
            deviceId,
            user.userId,
            "Android",
            "12",
            "1.0",
            "ko_kr"
        )
        def device = new Device(
            deviceId,
            user,
            request.platform,
            request.platformVersion,
            request.appVersion,
            request.locale,
            System.currentTimeMillis(),
            System.currentTimeMillis()
        )

        when:
        def result = service.upsertDevice(request, coroutineContext) as Device

        then:
        result == device
        1 * getDeviceQuery.getDeviceById(deviceId, _) >> null
        1 * createDeviceCommand.createDevice(request, _) >> device
        0 * _
    }

    def "update device"() {
        given:
        def deviceId = uuid
        def user = createUser()
        def request = new UpsertDeviceRequest(
            deviceId,
            user.userId,
            "Android",
            "12",
            "1.0",
            "ko_kr"
        )
        def device = new Device(
            deviceId,
            user,
            request.platform,
            request.platformVersion,
            request.appVersion,
            request.locale,
            System.currentTimeMillis(),
            System.currentTimeMillis()
        )

        when:
        def result = service.upsertDevice(request, coroutineContext) as Device

        then:
        result == device
        1 * getDeviceQuery.getDeviceById(deviceId, _) >> device
        1 * updateDeviceCommand.updateDevice(request, _) >> device
        0 * _
    }

}
