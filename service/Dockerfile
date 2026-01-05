# Stage 1: Build the application
FROM maven:3.9.9-eclipse-temurin-17-alpine AS builder

WORKDIR /build

# Copy pom.xml from service folder
COPY service/pom.xml ./pom.xml

# Download dependencies (cached layer)
RUN mvn dependency:go-offline -B

# Copy source code
COPY service/src ./src

# Build the app
RUN mvn clean package -DskipTests


# Stage 2: Runtime image
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the final jar
COPY --from=builder /build/target/*.jar app.jar

# Security: non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# JVM memory limits (important for Render Free)
ENV JAVA_OPTS="-XX:InitialRAMPercentage=70.0 -XX:MaxRAMPercentage=70.0"

EXPOSE 8088

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
