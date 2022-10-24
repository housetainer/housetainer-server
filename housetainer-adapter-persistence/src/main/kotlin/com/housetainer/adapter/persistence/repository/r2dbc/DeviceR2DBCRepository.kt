package com.housetainer.adapter.persistence.repository.r2dbc

import com.housetainer.adapter.persistence.repository.entity.DeviceEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface DeviceR2DBCRepository : CoroutineCrudRepository<DeviceEntity, Int> {

    suspend fun findByDeviceId(deviceId: String): DeviceEntity?
}
