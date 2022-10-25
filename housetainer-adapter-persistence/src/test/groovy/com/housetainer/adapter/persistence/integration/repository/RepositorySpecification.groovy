package com.housetainer.adapter.persistence.integration.repository

import com.housetainer.adapter.persistence.repository.DeviceRepository
import com.housetainer.adapter.persistence.repository.UserRepository
import com.housetainer.adapter.persistence.repository.r2dbc.DeviceR2DBCRepository
import com.housetainer.adapter.persistence.repository.r2dbc.R2dbcConfig
import com.housetainer.adapter.persistence.repository.r2dbc.UserR2DBCRepository
import com.housetainer.common.BaseSpecification
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.autoconfigure.data.r2dbc.AutoConfigureDataR2dbc
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@SpringBootTest(classes = [
    R2dbcConfig,
    UserR2DBCRepository,
    UserRepository,
    DeviceR2DBCRepository,
    DeviceRepository
])
@AutoConfigureDataR2dbc
@AutoConfigureTestEntityManager
@EnableR2dbcRepositories(basePackages = "com.housetainer.adapter.persistence.repository.r2dbc")
@EntityScan(basePackages = "com.housetainer.adapter.persistence.repository.entity")
abstract class RepositorySpecification extends BaseSpecification {
}
