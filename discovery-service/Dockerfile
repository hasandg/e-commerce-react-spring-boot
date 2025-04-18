FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/discovery-service-*.jar discovery-service.jar

EXPOSE 8761

CMD ["java", "-jar", "discovery-service.jar"] 