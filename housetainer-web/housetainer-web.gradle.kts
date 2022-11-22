import kotlinx.kover.api.CoverageEngine

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("org.jetbrains.kotlinx.kover")
    kotlin("kapt")
    kotlin("plugin.spring")
    application
}

application {
    mainClass.set("com.housetainer.HousetainerApplicationKt")
}

dependencies {
    val jacksonVersion: String by project

    implementation(project(":housetainer-common"))
    implementation(project(":housetainer-domain"))
    implementation(project(":housetainer-application"))
    implementation(project(":housetainer-adapter-web"))
    implementation(project(":housetainer-adapter-persistence"))

    // Spring
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // Log
    implementation("org.codehaus.janino:janino:3.0.6")

    // Jackson
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")

    // Swagger
    implementation("org.springdoc:springdoc-openapi-common:1.6.12")
    implementation("org.springdoc:springdoc-openapi-webflux-ui:1.6.12")

    // Test
    testImplementation(testFixtures(project(":housetainer-common")))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}

tasks.getByName<Jar>("jar") {
    enabled = false
}

tasks {
    kover {
        isDisabled = false
        coverageEngine.set(CoverageEngine.INTELLIJ)
        generateReportOnCheck = true

        koverHtmlReport {
            isEnabled = true
        }

        koverXmlReport {
            isEnabled = true
        }
    }

    koverVerify {
        rule {
            name = "Minimal line coverage rate in percents"
            bound {
                minValue = 0  // TODO change this value to 80 when test code is ready
            }
        }
    }

    test {
        if (project.hasProperty("housetainer.flyway.locations")) {
            systemProperty(
                "housetainer.flyway.locations",
                project.property("housetainer.flyway.locations").toString()
            )
        }
    }
}
