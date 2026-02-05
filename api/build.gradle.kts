import com.epages.restdocs.apispec.gradle.OpenApi3Task

plugins {
    alias(libs.plugins.restdocs.api.spec)
}

dependencies {
    implementation(project(":core"))
    implementation(libs.spring.web)
    implementation(libs.spring.security)
    implementation(libs.spring.validation)
    implementation(libs.spring.actuator)
    implementation(libs.spring.log4j2)
    implementation(libs.bundles.jwt)
    implementation(libs.bundles.jackson)
    implementation(libs.springdoc.openapi)

    testImplementation(testFixtures(project(":core")))
    testImplementation(libs.bundles.spring.test)
    testImplementation(libs.bundles.spring.restdocs)

    testFixturesImplementation(testFixtures(project(":core")))
    testFixturesImplementation(libs.bundles.test)
    testFixturesImplementation(libs.bundles.spring.test)
    testFixturesImplementation(libs.bundles.spring.restdocs)
    testFixturesImplementation(libs.spring.security)
}

tasks {
    bootJar {
        enabled = true
    }

    jar {
        enabled = false
    }

    test {
        finalizedBy(withType<OpenApi3Task>())
    }

    withType<OpenApi3Task> {
        doFirst {
            file(openapi3.outputDirectory).mkdirs()
        }
    }
}

openapi3 {
    title = "Retoday API"
    description = "Retoday API Documentation"
    version = project.version.toString()
    format = "yaml"
    outputFileNamePrefix = "api"
    outputDirectory = "src/main/resources/static/docs"
    setServer("/api/v1")
}
