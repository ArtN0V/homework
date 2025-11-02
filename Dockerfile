FROM openjdk:17-jdk-slim-buster
WORKDIR /app
COPY target/MyHomeWork-1.0-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
