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
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class DeviceRepository(
    private val repository: DeviceR2DBCRepository,
    private val userRepository: UserRepository
) : CreateDeviceCommand, GetDeviceByIdQuery {

    override suspend fun createDevice(upsertDeviceRequest: UpsertDeviceRequest): Device {
        val savedDevice: DeviceEntity? = repository.findByDeviceId(upsertDeviceRequest.deviceId)
        val now = Instant.now().toEpochMilli()

        return repository.execute {
            it.save(
                DeviceEntity(
                    id = savedDevice?.id,
                    deviceId = upsertDeviceRequest.deviceId,
                    userId = upsertDeviceRequest.userId,
                    platform = upsertDeviceRequest.platform,
                    platformVersion = upsertDeviceRequest.platformVersion,
                    appVersion = upsertDeviceRequest.appVersion,
                    locale = upsertDeviceRequest.locale,
                    createTime = savedDevice?.createTime ?: now,
                    updateTime = now,
                )
            )
        }.toDevice()
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
