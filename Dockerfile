FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY . .
RUN ./gradlew build -x test

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar event_service.jar
EXPOSE 8091

ENV SPRING_DOCKER_COMPOSE_ENABLED=false

ENTRYPOINT ["java", "-Dspring.main.class=com.gb.wellness.event_service.EventServiceApplication", "-jar", "event_service.jar"]

