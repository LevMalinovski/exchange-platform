FROM eclipse-temurin:21-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the built jar into the container
COPY build/libs/exchange-platform-0.0.1-SNAPSHOT.jar exchange-platform.jar

# Run the jar
ENTRYPOINT ["java", "-jar", "exchange-platform.jar"]