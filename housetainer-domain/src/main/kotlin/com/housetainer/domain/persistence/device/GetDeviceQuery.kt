package com.housetainer.domain.persistence.device

import com.housetainer.domain.entity.device.Device

interface GetDeviceQuery {

    suspend fun getDevice(deviceId: String): Device?
}
