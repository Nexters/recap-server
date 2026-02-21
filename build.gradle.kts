import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.java.library)
    alias(libs.plugins.java.test.fixtures)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring) apply false
    alias(libs.plugins.kotlin.lint) apply false
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependency.management)
}

allprojects {
    group = "com.retoday"
    version = "0.0.1"

    repositories {
        google()
        mavenCentral()
    }
}

subprojects {
    fun apply(provider: Provider<PluginDependency>) {
        apply(plugin = provider.get().pluginId)
    }

    apply(rootProject.libs.plugins.java.library)
    apply(rootProject.libs.plugins.java.test.fixtures)
    apply(rootProject.libs.plugins.kotlin.jvm)
    apply(rootProject.libs.plugins.kotlin.spring)
    apply(rootProject.libs.plugins.kotlin.lint)
    apply(rootProject.libs.plugins.spring.boot)
    apply(rootProject.libs.plugins.spring.dependency.management)

    java {
        sourceCompatibility = JavaVersion.VERSION_21
    }

    kotlin {
        compilerOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = JvmTarget.JVM_21
        }
    }

    configurations {
        all {
            exclude("org.springframework.boot", "spring-boot-starter-logging")
        }
    }

    dependencies {
        implementation(rootProject.libs.kotlin.logging)
        implementation(rootProject.libs.kotlin.reflect)
        testImplementation(rootProject.libs.bundles.test)
    }

    tasks {
        test {
            useJUnitPlatform()
            classpath += files(sourceSets.main.map { it.output })
        }
    }
}
