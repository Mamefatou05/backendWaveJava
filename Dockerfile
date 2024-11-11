FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/backendwave-0.0.1-SNAPSHOT.jar app.jar
ENV PORT=8081
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]
