FROM eclipse-temurin:24-jre

WORKDIR /app

COPY target/config-server-*.jar config-server.jar

EXPOSE 8888

CMD ["java", "-jar", "config-server.jar"] 