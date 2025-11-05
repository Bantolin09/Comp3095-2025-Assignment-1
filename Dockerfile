FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app
COPY . .
RUN ./gradlew build -x test

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar event-service.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=event
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/wellness_db
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=postgres
ENV WELLNESS_RESOURCE_SERVICE_URL=http://wellness-resource-service:8080

ENTRYPOINT ["java", "-jar", "event-service.jar"]