plugins {
    kotlin("kapt")
    id("java-test-fixtures")
}

dependencies {
    val groovyVersion: String by project
    val spockVersion: String by project

    val coroutineVersion: String by project
    val jacksonVersion: String by project

    // coroutine
    api("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$coroutineVersion")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:$coroutineVersion")

    // Log
    api("org.slf4j:slf4j-api:1.7.36")
    api("ch.qos.logback:logback-classic:1.2.11")
    api("org.codehaus.janino:janino:3.0.6")

    // Jackson
    api("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    api("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    api("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    api("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

    testFixturesApi("org.codehaus.groovy:groovy-all:$groovyVersion")
    testFixturesApi("org.spockframework:spock-core:$spockVersion")
    testFixturesApi("org.spockframework:spock-spring:$spockVersion")
    testFixturesApi("com.github.tomakehurst:wiremock-standalone:2.27.2")
}
