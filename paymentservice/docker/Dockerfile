FROM openjdk:17-jdk-slim
WORKDIR /app

ENV TARGET_DIR=target

# Copy Jar file 
COPY ${TARGET_DIR}/payment-service.jar app.jar

EXPOSE 12302
EXPOSE 12303
ENTRYPOINT ["java", "-jar", "app.jar"]