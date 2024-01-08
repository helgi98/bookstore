FROM openjdk:17
EXPOSE 8080
WORKDIR /app
COPY ./bookstore-starter/build/libs/bookstore-starter-0.0.1-SNAPSHOT.jar ./app.jar
CMD ["java", "-jar", "app.jar"]