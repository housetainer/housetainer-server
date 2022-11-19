package com.housetainer.domain.persistence.auth

import com.housetainer.domain.model.auth.UserProfileResponse

interface GetUserProfileFromNaverQuery {
    suspend fun getUserProfile(accessToken: String): UserProfileResponse
}
