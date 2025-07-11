
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Set working directory inside the container
WORKDIR /app

# Copy the project files
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

COPY src ./src

# Package the application (skip tests for faster build)
RUN ./mvnw clean package -DskipTests


FROM eclipse-temurin:21-jdk-alpine AS runner

# Set working directory
WORKDIR /app

# Copy the jar from the build stage
COPY --from=builder /app/target/*.jar app.jar

# Expose app port
EXPOSE 8887

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
