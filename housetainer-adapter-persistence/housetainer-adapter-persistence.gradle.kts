import kotlinx.kover.api.CoverageEngine

plugins {
    kotlin("kapt")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("org.jetbrains.kotlinx.kover")
}

dependencies {
    val flywayVersion: String by project

    implementation(project(":housetainer-common"))
    implementation(project(":housetainer-domain"))

    // Spring
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j")
    implementation(
        group = "io.netty",
        name = "netty-resolver-dns-native-macos",
        version = "4.1.72.Final",
        classifier = "osx-aarch_64"
    ) // for M1 Macbook

    // Database
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("com.github.jasync-sql:jasync-r2dbc-mysql:2.0.7")
    implementation("mysql:mysql-connector-java:8.0.30")
    implementation("org.flywaydb:flyway-core:$flywayVersion")

    // Test
    testImplementation(testFixtures(project(":housetainer-common")))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.flywaydb:flyway-mysql:$flywayVersion")
}

tasks.getByName<Jar>("bootJar") {
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
                minValue = 0
            }
        }
    }
}
