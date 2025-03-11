FROM openjdk:17-jdk-slim

WORKDIR /app

COPY hello-world-profiles/target/hello-world-profiles-0.0.1.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
