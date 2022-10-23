package com.housetainer.domain.model.device

data class UpsertDeviceRequest(
    val deviceId: String,
    val userId: String,
    val platform: String,
    val platformVersion: String,
    val appVersion: String,
    val locale: String
)
