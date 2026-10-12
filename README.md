# E-commerce Warehouse API

A RESTful backend for an e-commerce and warehouse system built with Spring boot. It manages categories, products, users and orders with stock control and transactional order processing.

## Tech Stack

| Area | Technology |
|----- | ---------- |
| Language | Java 17 |
| Framework | Spring Boot 4.1.0 (Web MVC, Data JPA, Security, Validation) |
| Database | PostgreSQL |
| Build tool | Gradle |
| Containerization | Docker (PostgreSQL container) |
| Other | Lombok, BCrypt password hashing|

##Features

- CRUD-style APIs for catagories, prodcuts, orders and users
- Order creation with automatic stock validation and stock reduction
- Order total calculated on the server; the product price is frozen at purchase time ('priceAtPurchase')
- '@Transactional' order processing: if any item fails, the whole order is rolled back and stock stay unchanged
- Passwords are hashed with BCrypt and never returned in responses
- Request validation with Jakarta Validation
- Global exception handling with proper HTTP status codes (404, 400)
- Separate request/response DTOs, so entities are never exposed directly

## Project Structure

```
src/main/java/az/ingress/e_commerce
├── config        # Security and password encoder configuration
├── controller    # REST endpoints
├── service       # Business logic
├── repository    # Spring Data JPA repositories
├── entity        # JPA entities
├── dto           # Request / response objects
├── enums         # Role, OrderStatus
└── exception     # Custom exceptions and global handler
```

## Getting Started

### Prerequisites
- Java 17
- Docker

### 1. Start PostgerSQL

```bash
docker run --name ecommerce-postgres \
  -e POSTGRES_USER=user \
  -e POSTGRES_PASSWORD=password \
  -e POSTGRES_DB=ecommerce_warehouse \
  -p 5433:5432 \
  -d postgres:18
```

### 2. Run the application

```bash
./gradlew bootRun
```

The API starts on `http://localhost:8080`. Tables are created automatically by Hibernate.

> Note: the database credentials in 'application.yaml' are for local development only.

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/categories | Create a category |
| GET | /api/categories | List all categories |
| GET | /api/categories/{id} | Get a category by id |
| POST | /api/products | Create a product |
| GET | /api/products | List all products |
| GET | /api/products/{id} | Get a product by id |
| POST | /api/users | Create a user |
| GET | /api/users | List all users |
| GET | /api/users/{id} | Get a user by id |
| POST | /api/orders | Create an order |
| GET | /api/orders | List all orders |
| GET | /api/orders/{id} | Get an order by id |

## Example: Create an order

**Request** 'POST /api/orders'

```json
{
  "userId": 1,
  "items": [
    { "productId": 1, "quantity": 2 }
  ]
}
```

**Response**

```json
{
  "id": 3,
  "orderDate": "2026-10-12T15:41:39.6021991",
  "status": "PENDING",
  "totalPrice": 2400.00,
  "items": [
    {
      "productName": "Laptop",
      "quantity": 2,
      "priceAtPurchase": 1200.00
    }
  ]
}
```

## Error Handling

| Situation          | HTTP status |
|--------------------| ----------- |
| Resource not found | 404 Not Found |
| Insufficient stock | 400 Bad Request |
| Validation failure | 400 Bad Request |

## Future Improvements

- JWT authentication and role-based authorization
- Swagged / OpenAPI documentation
- Unit and integration tests
- Dockerize the application with docker-compose

