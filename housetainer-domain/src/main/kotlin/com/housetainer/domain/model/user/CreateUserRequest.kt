package com.housetainer.domain.model.user

import com.housetainer.domain.entity.auth.AuthProvider
import com.housetainer.domain.entity.user.UserStatus
import com.housetainer.domain.entity.user.UserType

data class CreateUserRequest(
    val email: String,
    val authId: String,
    val authProvider: AuthProvider,
    val name: String,
    val nickname: String?,
    val gender: String?,
    val birthday: String?,
    val phoneNumber: String?,
    val profileImage: String?,
    val countryCode: String?,
    val languageCode: String?,
    val type: UserType,
    val status: UserStatus,
    val createTime: Long,
    val updateTime: Long
)
