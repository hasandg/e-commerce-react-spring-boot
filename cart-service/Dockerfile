FROM eclipse-temurin:24-jre

WORKDIR /app

COPY target/cart-service-0.0.1-SNAPSHOT.jar cart-service.jar

EXPOSE 8083

CMD ["java", "-jar", "cart-service.jar"] 