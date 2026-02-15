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
    implementation(libs.hypersistence.utils)
    implementation(libs.bundles.jwt)
    implementation(libs.gemini.google)
    implementation(libs.jackson.module.kotlin)
    runtimeOnly(libs.mysql.connector)

    testFixturesImplementation(libs.bundles.test)
    testImplementation(libs.bundles.spring.test)
}

tasks {
    bootJar {
        enabled = false
    }
}
