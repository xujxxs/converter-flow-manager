FROM gradle:jdk21 AS build
WORKDIR /app

COPY settings.gradle gradlew build.gradle ./
COPY gradle ./gradle
COPY src ./src

RUN ./gradlew build -x test

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/build/libs/flow-manager-0.0.1-SNAPSHOT.jar flow-manager.jar
EXPOSE 8080
CMD [ "java", "-jar", "flow-manager.jar" ]