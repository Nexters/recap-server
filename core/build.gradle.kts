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
}

tasks {
    bootJar {
        enabled = false
    }
}
