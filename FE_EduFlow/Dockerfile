# ==============================================================================
# EduFlow Frontend – Multi-stage Dockerfile
# ==============================================================================

# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests -B -Dfile.encoding=UTF-8

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Create non-root user
RUN addgroup -S eduflow && adduser -S eduflow -G eduflow

# Copy JAR from build stage
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar
# AppConfig creates upload subdirectories during startup. Prepare the writable
# root before dropping privileges so the non-root runtime user can start and
# can persist uploaded files for the lifetime of the Fargate task.
RUN mkdir -p uploads && chown eduflow:eduflow app.jar uploads

USER eduflow

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/ || exit 1

ENTRYPOINT ["java", \
  "-Xms256m", "-Xmx512m", \
  "-XX:+UseG1GC", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
