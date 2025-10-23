# Stage 1: Build the application using Maven and JDK 23
FROM maven:3.9-eclipse-temurin-23 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and download dependencies to leverage Docker layer caching
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy your application's source code
COPY src ./src

# Build the application and create the executable JAR, skipping tests
RUN mvn clean install -DskipTests


# Stage 2: Create the final, lightweight runtime image
# Use the general '23' tag which defaults to the latest supported OS
FROM eclipse-temurin:23

# Set the working directory for the runtime
WORKDIR /app

# Copy the JAR file from the 'build' stage's /app/target directory
COPY --from=build /app/target/userservice-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the application will run on (Spring Boot's default)
EXPOSE 9000

# The command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]
