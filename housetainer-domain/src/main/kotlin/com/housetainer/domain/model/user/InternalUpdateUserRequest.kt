package com.housetainer.domain.model.user

import com.housetainer.domain.entity.user.UserStatus
import com.housetainer.domain.entity.user.UserType

data class InternalUpdateUserRequest(
    val userId: String,
    var nickname: String? = null,
    var gender: String? = null,
    var birthday: String? = null,
    var phoneNumber: String? = null,
    var profileImage: String? = null,
    var countryCode: String? = null,
    var languageCode: String? = null,
    var type: UserType? = null,
    var status: UserStatus? = null
)
