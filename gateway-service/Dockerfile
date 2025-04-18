FROM eclipse-temurin:24-jre

WORKDIR /app

COPY target/gateway-service-*.jar gateway-service.jar

EXPOSE 8080

CMD ["java", "-jar", "gateway-service.jar"] 