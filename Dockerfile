FROM maven:3.9.9-eclipse-temurin-22-jammy as build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:22-jdk
COPY --from=build /target/SwingWebsocket-0.0.1-SNAPSHOT.jar SwingWebsocket.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "SwingWebsocket.jar"]