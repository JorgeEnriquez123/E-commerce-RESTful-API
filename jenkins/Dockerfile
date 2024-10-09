# DOCKERFILE For Jenkins
FROM openjdk:17
EXPOSE 8080
ADD target/ecommerce-api.jar ecommerce-api.jar
ENTRYPOINT ["java", "-jar", "/ecommerce-api.jar"]
