package com.housetainer.domain.model.auth

data class UserProfileResponse(
    val authId: String,
    val email: String,
    val name: String,
    val nickname: String?,
    val gender: String?,
    val birthday: String?,
    val profileImage: String?,
    val phoneNumber: String?
)
