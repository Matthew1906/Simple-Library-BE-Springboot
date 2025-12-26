# Use Java 21 (LTS)
FROM eclipse-temurin:21-jdk-jammy

# Set working directory
WORKDIR /app

# Copy Gradle wrapper and build files
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Copy source code
COPY src src

# Make gradlew executable
RUN chmod +x ./gradlew

# Build the Spring Boot fat JAR
RUN ./gradlew clean bootJar --no-daemon

RUN ls -R /app

# Copy the generated JAR to a standard location
# The JAR path should match your project
COPY build/libs/simple-library-project-be-0.0.1-SNAPSHOT.jar app.jar

# Expose default Spring Boot port
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "/app.jar"]
