package com.housetainer.adapter.persistence.repository.entity

import com.housetainer.common.utils.CommonUtils
import com.housetainer.domain.entity.auth.AuthProvider
import com.housetainer.domain.entity.user.UserStatus
import com.housetainer.domain.entity.user.UserType
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("users")
class UserEntity @JvmOverloads constructor(
    @Id var id: Long? = null,
    val userId: String = CommonUtils.randomUuid,
    val email: String,
    val authId: String,
    val authProvider: AuthProvider,
    val name: String,
    var nickname: String? = null,
    var gender: String? = null,
    var birthday: String? = null,
    var phoneNumber: String? = null,
    var profileImage: String? = null,
    var countryCode: String? = null,
    var languageCode: String? = null,
    var type: UserType,
    var status: UserStatus,
    var createTime: Long,
    var updateTime: Long
)
