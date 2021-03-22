FROM openjdk:8-jdk-alpine as build

WORKDIR /temp

COPY gradle gradle
COPY build.gradle .
COPY gradlew .
COPY gradlew.bat .
COPY settings.gradle .
COPY src src

RUN chmod +x ./gradlew
RUN ./gradlew build -x test

FROM openjdk:8-jdk-alpine
RUN addgroup -S lottery-group && adduser -S lottery-user -G lottery-group
COPY --from=build /temp/build/libs/ticket-service-*.jar /app/ticket-service.jar

USER lottery-user
ENTRYPOINT ["java", "-jar", "/app/ticket-service.jar"]
EXPOSE 80