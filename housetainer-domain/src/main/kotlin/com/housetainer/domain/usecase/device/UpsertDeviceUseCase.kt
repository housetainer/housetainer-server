package com.housetainer.domain.usecase.device

import com.housetainer.domain.entity.device.Device
import com.housetainer.domain.model.device.UpsertDeviceRequest

interface UpsertDeviceUseCase {

    suspend fun upsertDevice(upsertDeviceRequest: UpsertDeviceRequest): Device
}
