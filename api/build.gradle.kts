dependencies {
    implementation(project(":core"))
    implementation(libs.spring.web)
    implementation(libs.spring.security)
    implementation(libs.spring.validation)
    implementation(libs.bundles.jackson)

    testImplementation(testFixtures(project(":core")))
    testImplementation(libs.bundles.spring.test)

    testFixturesImplementation(testFixtures(project(":core")))
    testFixturesImplementation(libs.bundles.test)
    testFixturesImplementation(libs.bundles.spring.test)
    testFixturesImplementation(libs.spring.security)
}

tasks {
    bootJar {
        enabled = true
    }

    jar {
        enabled = false
    }
}
