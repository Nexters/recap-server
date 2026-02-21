# Build stage
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY settings.gradle.kts .
COPY build.gradle.kts .
COPY gradle/libs.versions.toml gradle/

COPY api api
COPY batch batch
COPY core core

RUN chmod +x gradlew && ./gradlew :api:bootJar :batch:bootJar --no-daemon

# Runtime stage for API
FROM eclipse-temurin:21-jre-alpine AS api
WORKDIR /app

RUN apk add --no-cache curl

COPY --from=build /app/api/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

# Runtime stage for Batch
FROM eclipse-temurin:21-jre-alpine AS batch
WORKDIR /app

COPY --from=build /app/batch/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
