FROM maven:3.9.11-eclipse-temurin-11-alpine AS build

WORKDIR /app

COPY . .

RUN mvn clean install -DskipTests

FROM openjdk:8-jre-alpine3.9

WORKDIR /app

COPY --from=build /app/target/client-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
