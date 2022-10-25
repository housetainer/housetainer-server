package com.housetainer.module

import com.housetainer.application.auth.SignService
import com.housetainer.domain.persistence.auth.GetUserProfileFromNaverQuery
import com.housetainer.domain.persistence.user.GetUserByEmailQuery
import com.housetainer.domain.usecase.device.UpsertDeviceUseCase
import com.housetainer.domain.usecase.user.CreateUserUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AuthModule {

    @Bean
    fun signService(
        getUserProfileFromNaverQuery: GetUserProfileFromNaverQuery,
        getUserByEmailQuery: GetUserByEmailQuery,
        createUserUseCase: CreateUserUseCase,
        upsertDeviceUseCase: UpsertDeviceUseCase
    ) = SignService(
        getUserProfileFromNaverQuery, getUserByEmailQuery, createUserUseCase, upsertDeviceUseCase
    )

}
