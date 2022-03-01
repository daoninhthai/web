# ============================================================================
# Multi-stage Dockerfile for EngWeb (Spring Boot + Vanilla JS)
# ============================================================================

# ── Stage 1: Build Spring Boot Backend ───────────────────────────────────────
FROM maven:3.9-eclipse-temurin-17 AS backend-build

WORKDIR /app
COPY be-english/EnglishLearningWeb/pom.xml ./
RUN mvn dependency:go-offline -B
COPY be-english/EnglishLearningWeb/src ./src
RUN mvn clean package -DskipTests

# ── Stage 2: Production Runtime ──────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine AS production

LABEL maintainer="developer"
LABEL description="English Learning Web Platform"

WORKDIR /app

# Copy backend JAR
COPY --from=backend-build /app/target/*.jar app.jar

# Copy frontend static files
COPY fe-english/FE/public/ /app/static/public/
COPY fe-english/FE/src/ /app/static/src/

# Create non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
