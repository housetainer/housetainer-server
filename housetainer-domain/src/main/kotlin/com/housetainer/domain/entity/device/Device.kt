package com.housetainer.domain.entity.device

import com.housetainer.domain.entity.user.User

data class Device(
    val deviceId: String,
    val user: User,
    val platform: String,
    val platformVersion: String,
    val appVersion: String,
    val locale: String,
    val createTime: Long,
    val updateTime: Long
)
