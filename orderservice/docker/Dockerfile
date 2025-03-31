# Use a lightweight JDK image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy built jar
COPY target/order-service.jar app.jar

# Expose application port
EXPOSE 12300
EXPOSE 12301

# Run the service
ENTRYPOINT ["java", "-jar", "app.jar"]
