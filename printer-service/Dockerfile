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

RUN echo "Installing Font"
RUN apk --no-cache add msttcorefonts-installer fontconfig && \
   update-ms-fonts && \
   fc-cache -f
COPY --from=build temp/build/libs/printer-service-*.jar /app/printer-service.jar
EXPOSE 80
ENTRYPOINT ["java", "-jar", "/app/printer-service.jar"]
