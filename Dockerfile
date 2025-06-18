# ---- Build stage ----
FROM maven:3.9.4-eclipse-temurin-17 AS build

WORKDIR /app

# Copy source code
COPY . .

# Ensure mvnw is executable
RUN chmod +x mvnw

# Build the application without tests
RUN ./mvnw clean package -DskipTests

# ---- Runtime stage ----
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy WAR file from build stage
COPY --from=build /app/target/*.war app.war

# Expose the port (change if your Spring Boot runs on a different port)
EXPOSE 8081

# Start the app
ENTRYPOINT ["java", "-jar", "app.war"]
