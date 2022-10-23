package com.housetainer.domain.model.auth

import com.housetainer.domain.entity.auth.AuthProvider

data class SignUpRequest(
    val accessToken: String,
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

    val deviceId: String,
    val platform: String,
    val platformVersion: String,
    val appVersion: String,
    val locale: String
)
