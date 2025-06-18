# ---- Build stage ----
FROM maven:3-openjdk-17 AS build

WORKDIR /app

# Copy source code
COPY . .

# Build the application without tests
RUN mvn clean package -DskipTests && rm -rf /root/.m2/repository

# ---- Runtime stage ----
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy WAR file from build stage
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.war backend.war

# Expose the port (change if your Spring Boot runs on a different port)
EXPOSE 8081

# Start the app
ENTRYPOINT ["java", "-jar", "backend.war"]
