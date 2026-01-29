FROM docker.io/library/maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app
COPY . /app
RUN mvn clean package

FROM docker.io/library/eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
