# Stage 1: Build the application
FROM maven:3.9.9-eclipse-temurin-17-alpine AS builder

WORKDIR /build

# Copy only pom.xml first → great caching for dependencies
COPY pom.xml .
# If you have multiple modules, copy all pom.xml files here

# Download dependencies (cached layer)
RUN mvn dependency:go-offline -B

# Now copy source code
COPY src ./src

# Build the app (skip tests to make build faster on Render)
RUN mvn clean package -DskipTests

# Stage 2: Small runtime image
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy only the final jar from builder stage
COPY --from=builder /build/target/*.jar app.jar

# Create non-root user (security best practice)
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Recommended JVM flags for containers (adjust memory % based on your Render plan)
ENV JAVA_OPTS="-XX:InitialRAMPercentage=70.0 -XX:MaxRAMPercentage=70.0"

# Render automatically sets $PORT env variable → Spring Boot will use it
EXPOSE 8088

ENTRYPOINT ["java", "-jar", "/app/app.jar"]