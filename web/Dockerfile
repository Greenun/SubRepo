#FROM openjdk:11 AS builder
FROM gradle:6.4.1-jdk11 AS builder
COPY build.gradle .
COPY settings.gradle .
COPY gradlew .
COPY gradle gradle
COPY src ./src

RUN chmod +x ./gradlew
RUN ./gradlew --no-daemon bootJar

FROM openjdk:11
ENV APP_HOME = /usr/app/
WORKDIR ${APP_HOME}
COPY --from=builder /home/gradle/build/libs/*.jar ./app.jar

ARG ENVIRONMENT

ENV SPRING_PROFILES_ACTIVE=${ENVIRONMENT}

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "./app.jar"]