FROM amazoncorretto:21
VOLUME /app
ARG JAR_FILE=target/config-server-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]