FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# Copy everything
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

RUN chmod +x ./gradlew

# Build fat JAR inside Docker
RUN ./gradlew clean bootJar --no-daemon

# Use the generated JAR
RUN cp build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
