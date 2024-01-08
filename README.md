# Bookstore application

### Prerequisites

* JDK 17
* Docker
* Docker Compose

### How to build application

```shell
./gradlew build
```

### How to run application

```shell
docker-compose up -d --build bookstore
```

### How to stop application

```shell
docker-compose down
```

### How to test application

You can use swagger to test REST endpoints. Swagger is available at http://localhost:8080/swagger-ui.html

You can use graphiql to test GraphQL endpoints. Graphiql is available at http://localhost:8080/graphiql

Endpoints are secured. To use secured endpoints you need to create account and then login and use JWT token in
Authorization header. Use sign up and log in endpoints to create account and generate token. Then you can use the rest
of endpoints.

