package com.housetainer.domain.model.user

import com.housetainer.domain.entity.auth.AuthProvider
import com.housetainer.domain.entity.user.UserStatus
import com.housetainer.domain.entity.user.UserType

data class UserResponse(
    val userId: String,
    val email: String,
    val authId: String,
    val authProvider: AuthProvider,
    val name: String,
    val nickname: String? = null,
    val gender: String? = null,
    val birthday: String? = null,
    val phoneNumber: String? = null,
    val profileImage: String? = null,
    val countryCode: String? = null,
    val languageCode: String? = null,
    val type: UserType? = null,
    val status: UserStatus,
    val createTime: Long,
    val updateTime: Long
)
