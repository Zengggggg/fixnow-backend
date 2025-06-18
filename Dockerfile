# Stage 1: Build WAR from source
FROM maven:3-openjdk-17 AS build
WORKDIR /app

# Copy project files
COPY . .

# Build WAR file, skipping tests for faster build
RUN mvn clean package -DskipTests

# Stage 2: Run with Tomcat
FROM tomcat:9.0-jdk17-temurin

# Remove default webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy WAR file and rename it to ROOT.war (for root context)
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Set working directory (optional)
WORKDIR /usr/local/tomcat

# Expose port (optional for local, Render will auto expose PORT env)
EXPOSE 8080

# Start Tomcat server
CMD ["catalina.sh", "run"]
