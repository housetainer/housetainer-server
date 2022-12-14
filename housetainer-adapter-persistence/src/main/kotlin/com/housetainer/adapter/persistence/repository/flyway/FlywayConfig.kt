package com.housetainer.adapter.persistence.repository.flyway

import org.flywaydb.core.Flyway
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
class FlywayConfig(private val environment: Environment) {

    @Bean(initMethod = "migrate")
    fun flyway(): Flyway {
        return Flyway(
            Flyway.configure()
                .baselineOnMigrate(
                    environment.getProperty("spring.flyway.baseline-on-migrate", Boolean::class.java, true)
                )
                .dataSource(
                    environment.getRequiredProperty("spring.flyway.url"),
                    environment.getRequiredProperty("spring.flyway.user"),
                    environment.getRequiredProperty("spring.flyway.password")
                )
                .locations(locations())
        )
    }

    fun locations(): String = System.getProperty("housetainer.flyway.locations")
        ?: environment.getRequiredProperty("spring.flyway.locations")
}
