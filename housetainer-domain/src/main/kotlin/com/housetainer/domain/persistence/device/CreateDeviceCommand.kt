package com.housetainer.domain.persistence.device

import com.housetainer.domain.entity.device.Device
import com.housetainer.domain.model.device.UpsertDeviceRequest

interface CreateDeviceCommand {

    suspend fun createDevice(upsertDeviceRequest: UpsertDeviceRequest): Device
}
