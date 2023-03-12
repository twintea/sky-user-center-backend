FROM openjdk:8-jdk-alpine

# Copy local code to the container image.
WORKDIR /app

# Build a release artifact.
ADD ./sky-user-center-backend-0.0.1-SNAPSHOT.jar /app/sky-user-center-backend.jar

EXPOSE 8666
# Run the web service on container startup.
CMD ["java","-jar","/app/sky-user-center-backend.jar","--spring.profiles.active=prod"]