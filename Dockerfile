FROM jelastic/maven:3.9.5-openjdk-21 AS build

LABEL authors="Sambhav Mahajan"
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/SimpleForums-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]