# Basic E-commerce RESTful API
Simple E-commerce RESTful API built with Spring boot and MySQL, featuring simple operations.
This project's main purpose is to showcase some of the most used technologies when developing a REST API with Spring Boot.

## Main Tools:
<div align="center">
	<code><img width="60" src="https://user-images.githubusercontent.com/25181517/183891303-41f257f8-6b3d-487c-aa56-c497b880d0fb.png" alt="Spring Boot" title="Spring Boot"/></code>
	<code><img width="60" src="https://user-images.githubusercontent.com/25181517/117207493-49665200-adf4-11eb-808e-a9c0fcc2a0a0.png" alt="Hibernate" title="Hibernate"/></code>
	<code><img width="60" src="https://user-images.githubusercontent.com/25181517/183896128-ec99105a-ec1a-4d85-b08b-1aa1620b2046.png" alt="MySQL" title="MySQL"/></code>
	<code><img width="60" src="https://user-images.githubusercontent.com/25181517/182884894-d3fa6ee0-f2b4-4960-9961-64740f533f2a.png" alt="redis" title="redis"/></code>
	<code><img width="60" src="https://user-images.githubusercontent.com/25181517/186711335-a3729606-5a78-4496-9a36-06efcc74f800.png" alt="Swagger" title="Swagger"/></code>
	<code><img width="60" src="https://user-images.githubusercontent.com/25181517/117533873-484d4480-afef-11eb-9fad-67c8605e3592.png" alt="JUnit" title="JUnit"/></code>
	<code><img width="60" src="https://user-images.githubusercontent.com/25181517/183892181-ad32b69e-3603-418c-b8e7-99e976c2a784.png" alt="mockito" title="mockito"/></code>
	<code><img width="60" src="https://user-images.githubusercontent.com/25181517/117207330-263ba280-adf4-11eb-9b97-0ac5b40bc3be.png" alt="Docker" title="Docker"/></code>
	<code><img width="60" src="https://user-images.githubusercontent.com/25181517/182534006-037f08b5-8e7b-4e5f-96b6-5d2a5558fa85.png" alt="Kubernetes" title="Kubernetes"/></code>
	<code><img width="60" src="https://user-images.githubusercontent.com/25181517/179090274-733373ef-3b59-4f28-9ecb-244bea700932.png" alt="Jenkins" title="Jenkins"/></code>
	<code><img width="60" src="https://user-images.githubusercontent.com/25181517/184146221-671413cb-b1ae-47db-a232-b37c99281516.png" alt="SonarQube" title="SonarQube"/></code>
</div>

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

### Deployment & Pipelines
* Dockerfile
* Docker-compose
* Kubernetes yaml files
* Helm approach for Kubernetes deployment
* Jenkins
* SonarQube

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

