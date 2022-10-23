package com.housetainer.application.device

import com.housetainer.common.log.logger
import com.housetainer.domain.entity.device.Device
import com.housetainer.domain.model.device.UpsertDeviceRequest
import com.housetainer.domain.persistence.device.CreateDeviceCommand
import com.housetainer.domain.persistence.device.GetDeviceQuery
import com.housetainer.domain.persistence.device.UpdateDeviceCommand
import com.housetainer.domain.usecase.device.UpsertDeviceUseCase

class DeviceService(
    private val getDeviceQuery: GetDeviceQuery,
    private val createDeviceCommand: CreateDeviceCommand,
    private val updateDeviceCommand: UpdateDeviceCommand,
) : UpsertDeviceUseCase {

    private val log = logger()

    override suspend fun upsertDevice(upsertDeviceRequest: UpsertDeviceRequest): Device {
        val savedDevice = getDeviceQuery.getDevice(upsertDeviceRequest.deviceId)

        return if (savedDevice == null) {
            log.info("create-user-device, request={}", upsertDeviceRequest)
            createDeviceCommand.createDevice(upsertDeviceRequest)
        } else {
            log.info("update-user-device, request={}", upsertDeviceRequest)
            updateDeviceCommand.updateDevice(upsertDeviceRequest)
        }
    }
}
