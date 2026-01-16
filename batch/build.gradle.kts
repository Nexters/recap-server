dependencies {
    implementation(project(":core"))
    implementation(libs.spring.batch)
    implementation(libs.spring.log4j2)
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
