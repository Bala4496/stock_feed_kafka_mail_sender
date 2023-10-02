# Building the Spring Boot JAR
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -Dmaven.test.skip

# Creating the Docker image
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/Stock_Feed_Kafka_Mail_Sender-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9010
CMD ["java", "-jar", "app.jar"]