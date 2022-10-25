package com.housetainer.adapter.persistence.repository.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("devices")
class DeviceEntity @JvmOverloads constructor(
    @Id var id: Long? = null,
    val deviceId: String,
    val userId: String,
    var platform: String? = null,
    var platformVersion: String? = null,
    var appVersion: String? = null,
    var locale: String? = null,
    var pushProvider: String? = null,
    var pushAppId: String? = null,
    var pushRegId: String? = null,
    var createTime: Long,
    var updateTime: Long
)
