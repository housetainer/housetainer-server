package com.housetainer.domain.entity.user

import com.housetainer.domain.entity.auth.AuthProvider

data class User(
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
