package com.housetainer.domain.persistence.device

import com.housetainer.domain.entity.device.Device

interface GetDeviceByIdQuery {

    suspend fun getDeviceById(deviceId: String): Device?
}
