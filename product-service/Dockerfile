FROM eclipse-temurin:24-jre

WORKDIR /app

COPY target/product-service-*.jar product-service.jar

EXPOSE 8082

CMD ["java", "-jar", "product-service.jar"] 