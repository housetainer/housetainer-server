package com.housetainer.adapter.persistence.repository.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(name = "devices")
class DeviceEntity @JvmOverloads constructor(
    @Id var id: Int? = null,
    val deviceId: String,
    val userId: String,
    val platform: String,
    val platformVersion: String,
    val appVersion: String,
    val locale: String,
    var pushProvider: String? = null,
    var pushAppId: String? = null,
    var pushRegId: String? = null,
    var createTime: Long,
    var updateTime: Long
)
