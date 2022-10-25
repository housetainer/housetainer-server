package com.housetainer.adapter.persistence.repository

import com.housetainer.adapter.persistence.exception.PersistenceExceptions
import com.housetainer.adapter.persistence.repository.entity.DeviceEntity
import com.housetainer.adapter.persistence.repository.r2dbc.DeviceR2DBCRepository
import com.housetainer.adapter.persistence.repository.r2dbc.RepositoryUtils.execute
import com.housetainer.domain.entity.device.Device
import com.housetainer.domain.entity.user.User
import com.housetainer.domain.model.device.UpsertDeviceRequest
import com.housetainer.domain.persistence.device.CreateDeviceCommand
import com.housetainer.domain.persistence.device.GetDeviceByIdQuery
import com.housetainer.domain.persistence.device.UpdateDeviceCommand
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class DeviceRepository(
    private val repository: DeviceR2DBCRepository,
    private val userRepository: UserRepository
) : CreateDeviceCommand, GetDeviceByIdQuery, UpdateDeviceCommand {

    override suspend fun createDevice(upsertDeviceRequest: UpsertDeviceRequest): Device {
        return upsertDevice(upsertDeviceRequest)
    }

    override suspend fun updateDevice(upsertDeviceRequest: UpsertDeviceRequest): Device {
        return upsertDevice(upsertDeviceRequest, repository.findByDeviceId(upsertDeviceRequest.deviceId))
    }

    private suspend fun upsertDevice(
        upsertDeviceRequest: UpsertDeviceRequest,
        savedDevice: DeviceEntity? = null
    ): Device {
        val now = Instant.now().toEpochMilli()

        val device: DeviceEntity = savedDevice
            ?: DeviceEntity(
                deviceId = upsertDeviceRequest.deviceId,
                userId = upsertDeviceRequest.userId,
                createTime = now,
                updateTime = now,
            )

        device.platform = upsertDeviceRequest.platform ?: device.platform
        device.platformVersion = upsertDeviceRequest.platformVersion ?: device.platformVersion
        device.appVersion = upsertDeviceRequest.appVersion ?: device.appVersion
        device.locale = upsertDeviceRequest.locale ?: device.locale

        return repository.execute { it.save(device) }.toDevice()
    }

    override suspend fun getDeviceById(deviceId: String): Device? {
        return repository.findByDeviceId(deviceId)?.toDevice()
    }

    private suspend fun DeviceEntity.toDevice(): Device {
        val user: User = userRepository.getUserById(userId) ?: throw PersistenceExceptions.userNotFound()
        return Device(
            deviceId = deviceId,
            user = user,
            platform = platform,
            platformVersion = platformVersion,
            appVersion = appVersion,
            locale = locale,
            createTime = createTime,
            updateTime = updateTime,
        )
    }
}
