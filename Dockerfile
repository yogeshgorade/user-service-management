FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/*.jar user.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "user.jar"]