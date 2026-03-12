plugins {
    alias(libs.plugins.flyway)
}

dependencies {
    implementation(libs.spring.web)
    implementation(libs.spring.data.jdbc)
    implementation(libs.spring.data.redis)
    implementation(libs.spring.log4j2)
    implementation(libs.kotlin.logging)
    implementation(libs.flyway.core)
    implementation(libs.google.gemini)
    implementation(libs.jackson.kotlin)
    implementation(libs.bundles.jwt)
    runtimeOnly(libs.mysql.driver)
    runtimeOnly(libs.flyway.mysql)

    testImplementation(libs.spring.test)
    testFixturesImplementation(libs.spring.data.jdbc)
    testFixturesImplementation(libs.bundles.test)
    testFixturesImplementation(libs.bundles.spring.test)
    testFixturesImplementation(libs.bundles.testcontainers)
}

tasks {
    bootJar {
        enabled = false
    }
}
