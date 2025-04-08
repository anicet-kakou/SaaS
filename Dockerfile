# Stage 1: Build stage
FROM gradle:8.6-jdk21 AS build
WORKDIR /app

# Copy gradle configuration files
COPY build.gradle settings.gradle /app/
COPY gradle /app/gradle

# Download dependencies
RUN gradle dependencies --no-daemon

# Copy source code
COPY src /app/src

# Build the application
RUN gradle build --no-daemon -x test

# Stage 2: Runtime stage
FROM openjdk:21-slim
WORKDIR /app

# Install necessary tools
RUN apt-get update && apt-get install -y curl && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Copy the built JAR file
COPY --from=build /app/build/libs/*.jar /app/app.jar

# Expose the port
EXPOSE 8080

# Set entry point
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
