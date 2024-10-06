# Basic E-commerce RESTful API
Simple E-commerce RESTful API built with Spring boot and MySQL, featuring simple operations.
This project's main purpose is to showcase some of the most used technologies when developing a REST API with Spring Boot.

## Featuring:

### Modeling:
* Regular & Composite Entities
* DTO Mapping with ModelMapper
* Avoid exposure to Entity models on Controllers
* Lombok

### Persistence:
* Spring Data JPA
* Validation
* Pagination
* Custom Queries (JPQL & Native) when needed for better performance

### Security:
* JWT Security w/ Refresh Token mechanism
* RBAC
* Initial Roles loaded. (CUSTOMER and ADMIN)
* Authentication & Authorization error handling
* Caching Mechanism with Redis (For Authentication)

### Exception Handling
* Custom Exceptions
* Global Exception Handling
* Logging with Slf4j

### Documentation
* Swagger documentation with Springdoc OpenAPI (w/ manual operation setting)
* API Versioning

### Deployment
* Dockerfile
* Docker-compose
* Kubernetes yaml files
* Helm approach for Kubernetes deployment

## Flow:
- Each product has one Category associated to.
- A User has a 'CUSTOMER' role as default, but can have multiple roles, including 'ADMIN'.
- Users can have many Address Lines, which are referred as "Shipping Address" on their orders. They can add, remove or update their Address Lines. They can also set their default Address line.
- Each User has their own cart and can add or remove items from it, as well as update the quantity of their products in their cart. They can also check the contents of their cart.
- Users can place an order based on their cart's items and on their selected Address Line (Shipping Address).
- Users can check their orders and their details.
- An Admin can persist data that Customers can not (Products, Categories, Roles)

***


## To Run using Docker
1. Clone the repository.
2. Build and start the container:
```shell
docker-compose up --build
```

API documentation at: `http://localhost:8080/swagger-ui.html`

