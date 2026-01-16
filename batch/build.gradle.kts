dependencies {
    implementation(project(":core"))
    implementation(libs.spring.batch)
    implementation(libs.bundles.jackson)

    testImplementation(testFixtures(project(":core")))
    testImplementation(libs.bundles.spring.test)

    testFixturesImplementation(testFixtures(project(":core")))
}

tasks {
    bootJar {
        enabled = true
    }

    jar {
        enabled = false
    }
}
