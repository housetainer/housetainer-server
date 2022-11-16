package com.housetainer.module

import com.housetainer.application.auth.SignService
import com.housetainer.domain.persistence.user.GetUserByEmailQuery
import com.housetainer.domain.persistence.user.GetUserByIdQuery
import com.housetainer.domain.usecase.user.CreateUserUseCase
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
class AuthModule {

    @Bean
    fun signService(
        getUserByIdQuery: GetUserByIdQuery,
        getUserByEmailQuery: GetUserByEmailQuery,
        createUserUseCase: CreateUserUseCase,
        @Value("\${auth.userToken.secretKey}") tokenSecretKey: String,
        @Value("\${auth.userToken.timeout}") tokenTimeout: Duration
    ) = SignService(
        getUserByIdQuery, getUserByEmailQuery, createUserUseCase, tokenSecretKey, tokenTimeout
    )
}
