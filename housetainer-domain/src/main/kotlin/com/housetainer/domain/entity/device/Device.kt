package com.housetainer.domain.entity.device

import com.housetainer.domain.entity.user.User

data class Device(
    val deviceId: String,
    val user: User,
    var platform: String?,
    var platformVersion: String?,
    var appVersion: String?,
    var locale: String?,
    val createTime: Long,
    val updateTime: Long
)
