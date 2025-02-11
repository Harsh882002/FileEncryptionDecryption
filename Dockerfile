# Use Java 21 runtime as the base image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app
 
# Copy your Maven wrapper and pom.xml
COPY mvnw ./
COPY .mvn .mvn

# Copy the entire project into the container
COPY . .

# Ensure the Maven wrapper is executable
RUN chmod +x ./mvnw

# Build and package the application using the Maven 
RUN ./mvnw clean package -DskipTests
   
# Expose the port your application runs on
EXPOSE 8080

# Run the application (replace with your actual JAR file name)
CMD ["java", "-jar", "target/fileEncrypDecryp-0.0.1-SNAPSHOT.jar"]
