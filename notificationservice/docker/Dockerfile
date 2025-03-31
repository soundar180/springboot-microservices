FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/notification-service.jar app.jar

EXPOSE 12304
EXPOSE 12305

ENTRYPOINT ["java", "-jar", "app.jar"]
