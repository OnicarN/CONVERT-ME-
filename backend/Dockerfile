# ── Fase 1: Compilar el proyecto con Maven ──────────────────
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos el pom.xml primero (así Docker cachea las dependencias)
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Ahora copiamos el código y compilamos
COPY src ./src
RUN mvn clean package -DskipTests -q

# ── Fase 2: Imagen final solo con el JAR ────────────────────
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]