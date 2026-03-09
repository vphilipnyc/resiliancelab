# Stage 1: Build the application
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

# Copy gradle files first to leverage Docker layer caching
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Download dependencies (this will fail if no source, but caches the layers)
RUN ./gradlew build -x test --continue || true

# Copy source and build
COPY src src
RUN ./gradlew clean bootJar -x test

# Stage 2: Final Runtime Image
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Non-root user for security (stringent best practice)
RUN useradd -ms /bin/bash appuser
USER appuser

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]