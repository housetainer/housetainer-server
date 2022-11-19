package com.housetainer.adapter.persistence.webclient.client

import com.housetainer.adapter.persistence.http.webclient.factory.WebClientPair
import com.housetainer.adapter.persistence.model.NaverUserProfile
import com.housetainer.adapter.persistence.webclient.filter.WebClientFilter
import com.housetainer.common.log.logger
import com.housetainer.common.utils.CommonUtils
import com.housetainer.common.utils.Constants
import com.housetainer.domain.model.auth.UserProfileResponse
import com.housetainer.domain.persistence.auth.GetUserProfileFromNaverQuery
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component
import reactor.util.retry.Retry

@Component
@DependsOn("webClientFactory")
class NaverClient(
    @Qualifier("naverWebClientPair") webClientPair: WebClientPair
) : GetUserProfileFromNaverQuery {

    private val log = logger()

    private val webClient = webClientPair.webClient
    private val retrySpec = Retry.backoff(
        webClientPair.properties.maxRetry, webClientPair.properties.retryDelay
    ).filter { WebClientFilter.is5xxServerError(it) }

    override suspend fun getUserProfile(accessToken: String): UserProfileResponse {
        val userProfile: NaverUserProfile = webClient
            .get()
            .uri(GET_USER_PROFILE_URL)
            .header(Constants.AUTHORIZATION, CommonUtils.bearerToken(accessToken))
            .retrieve()
            .bodyToMono(NaverUserProfile::class.java)
            .retryWhen(retrySpec)
            .awaitSingle()

        log.info("naver-user-profile, profile={}", userProfile)
        return UserProfileResponse(
            userProfile.response.id,
            userProfile.response.email,
            userProfile.response.name,
            userProfile.response.nickname,
            userProfile.response.gender,
            userProfile.response.birthday,
            userProfile.response.profileImage,
            userProfile.response.mobile
        )
    }

    companion object {
        const val GET_USER_PROFILE_URL = "/v1/nid/me"
    }
}
