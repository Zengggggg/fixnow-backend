# ---- Build Stage ----
FROM maven:3.9.4-eclipse-temurin-17 AS build

WORKDIR /app

# Copy source code
COPY . .

# Build the application without running tests
RUN mvn clean package -DskipTests

# ---- Runtime Stage (Tomcat) ----
FROM tomcat:9.0-jdk17-temurin

# Xóa các ứng dụng mặc định trong Tomcat (tùy chọn)
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy WAR file từ stage build vào thư mục deploy của Tomcat
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Expose port 8080 (Render yêu cầu app chạy trên cổng này)
EXPOSE 8080
