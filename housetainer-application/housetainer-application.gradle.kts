import kotlinx.kover.api.CoverageEngine

plugins {
    kotlin("kapt")
    id("org.jetbrains.kotlinx.kover")
}

dependencies {
    implementation(project(":housetainer-common"))
    implementation(project(":housetainer-domain"))

    implementation("org.glassfish.jaxb:jaxb-runtime:2.3.2")
    implementation("io.jsonwebtoken:jjwt:0.9.1")

    // Test
    testImplementation(testFixtures(project(":housetainer-common")))
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
