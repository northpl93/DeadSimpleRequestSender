FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app
COPY . /app

RUN ./gradlew shadowJar --no-daemon

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app
COPY --from=build /app/application/build/libs/*-all.jar /app/
COPY --from=build /app/plugins/*/build/libs/*-all.jar /app/plugins/

CMD ["java", "--add-opens=java.base/java.nio=ALL-UNNAMED", "-jar", "/app/application-all.jar", "-daemon"]