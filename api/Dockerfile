# Use openjdk as the base image
FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy the built jar from your Gradle build output (./build/libs/api.jar)
COPY build/libs/api.jar /app/api.jar

# Expose the port that your Spring Boot application will run on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "api.jar"]
