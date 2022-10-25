package com.housetainer.domain.model.device

data class UpsertDeviceRequest @JvmOverloads constructor(
    val deviceId: String,
    val userId: String,
    var platform: String? = null,
    var platformVersion: String? = null,
    var appVersion: String? = null,
    var locale: String? = null
)
