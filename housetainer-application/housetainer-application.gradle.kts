import kotlinx.kover.api.CoverageEngine

plugins {
    kotlin("kapt")
    id("org.jetbrains.kotlinx.kover")
}

dependencies {
    implementation(project(":housetainer-common"))
    implementation(project(":housetainer-domain"))

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
