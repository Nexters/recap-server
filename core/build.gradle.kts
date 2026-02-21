plugins {
    alias(libs.plugins.kotlin.jpa)
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

dependencies {
    implementation(libs.spring.web)
    implementation(libs.spring.data.jpa)
    implementation(libs.spring.data.redis)
    implementation(libs.spring.log4j2)
    implementation(libs.hypersistence.utils)
    implementation(libs.google.gemini)
    implementation(libs.jackson.kotlin)
    implementation(libs.bundles.jdsl)
    implementation(libs.bundles.jwt)
    runtimeOnly(libs.mysql.connector)

    testImplementation(libs.spring.test)
    testImplementation(libs.h2)
    testFixturesImplementation(libs.spring.data.jpa)
    testFixturesImplementation(libs.jdsl.jpa)
    testFixturesImplementation(libs.bundles.test)
    testFixturesImplementation(libs.bundles.spring.test)
    testFixturesImplementation(libs.bundles.test.containers)
}

tasks {
    bootJar {
        enabled = false
    }
}
