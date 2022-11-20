package com.housetainer.domain.model.user

import com.housetainer.domain.entity.user.UserStatus
import com.housetainer.domain.entity.user.UserType

data class UpdateUserRequest(
    val userId: String,
    val nickname: String?,
    val gender: String?,
    val birthday: String?,
    val phoneNumber: String?,
    val profileImage: String?,
    val countryCode: String?,
    val languageCode: String?,
    val type: UserType?,
    val status: UserStatus?
)
