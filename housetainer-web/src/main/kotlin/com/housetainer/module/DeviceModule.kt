package com.housetainer.module

import com.housetainer.application.device.DeviceService
import com.housetainer.domain.persistence.device.CreateDeviceCommand
import com.housetainer.domain.persistence.device.GetDeviceByIdQuery
import com.housetainer.domain.persistence.device.UpdateDeviceCommand
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DeviceModule {

    @Bean
    fun deviceService(
        getDeviceByIdQuery: GetDeviceByIdQuery,
        createDeviceCommand: CreateDeviceCommand,
        updateDeviceCommand: UpdateDeviceCommand,
    ) = DeviceService(
        getDeviceByIdQuery, createDeviceCommand, updateDeviceCommand
    )

}
