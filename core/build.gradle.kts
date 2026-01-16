plugins {
    alias(libs.plugins.kotlin.jpa)
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

dependencies {
    implementation(libs.spring.data.jpa)
    runtimeOnly(libs.mysql.connector)

    testFixturesImplementation(libs.bundles.test)
}

tasks {
    bootJar {
        enabled = false
    }
}
