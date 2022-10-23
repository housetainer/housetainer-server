package com.housetainer.adapter.persistence.model

import com.fasterxml.jackson.annotation.JsonProperty

data class NaverUserProfile(
    @get:JsonProperty("resultcode") val resultCode: String,
    val message: String,
    val response: NaverUserProfileResponse
)

data class NaverUserProfileResponse(
    val id: String,
    val email: String,
    val name: String,
    val nickname: String?,
    val gender: String?,
    val age: String?,
    val birthday: String?,
    @get:JsonProperty("profile_image") val profileImage: String?,
    val mobile: String?
)
