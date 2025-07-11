

```markdown
# 🍽️ ByteBites - Secure Microservices Restaurant Platform

ByteBites is a secure, scalable, and fault-tolerant restaurant management platform built using Spring Boot Microservices architecture. It features JWT-based authentication, centralized configuration, service discovery, API gateway routing, asynchronous messaging with RabbitMQ, circuit breakers with Resilience4j, and distributed tracing with Zipkin.

---

## 📌 Project Highlights

- ✅ **Spring Boot Microservices** (Auth, Restaurant, Order, Notification)
- 🔐 **OAuth2 + JWT** for secure login and service-to-service authentication
- 🧭 **Spring Cloud Gateway** for routing and security
- 🧩 **Eureka** for service discovery
- ⚙️ **Spring Cloud Config Server** for centralized config management
- 🐇 **RabbitMQ** for async messaging (order → notification)
- 💥 **Resilience4j** for fault tolerance (circuit breakers, fallback)
- 📈 **Zipkin** for distributed tracing and monitoring

---

## 🧱 Flow Diagram

```  
<pre>``` mermaid flowchart TD
subgraph External
Client[Client - Browser/Postman]
OAuth[OAuth Provider - Google/GitHub]
end

subgraph Gateway Layer
Gateway[API Gateway - JWT Filter + Routing]
end

subgraph Infra
Eureka[Discovery Server - Eureka]
Config[Config Server]
end

subgraph Auth System
AuthService[auth-service - Login / Register / JWT]
UserService[user-service - User Profiles / Roles]
end

subgraph Business Services
RestaurantService[restaurant-service - Menus / Ownership]
OrderService[order-service - Create/View Orders]
NotificationService[notification-service - Order Events - Email/Push]
end

subgraph Messaging
Kafka[Kafka or RabbitMQ - Event Bus]
end

%% Flow
Client -->|Login| AuthService
AuthService -->|JWT| Client
Client -->|Requests + JWT| Gateway

Gateway -->|Route| AuthService
Gateway -->|Route| RestaurantService
Gateway -->|Route| OrderService
Gateway -->|Route| UserService

AuthService --> Config
RestaurantService --> Config
OrderService --> Config
NotificationService --> Config
UserService --> Config

Gateway --> Eureka
AuthService --> Eureka
RestaurantService --> Eureka
OrderService --> Eureka
NotificationService --> Eureka
UserService --> Eureka

OrderService -->|OrderPlacedEvent| Kafka
Kafka --> NotificationService
Kafka --> RestaurantService
```</pre>
```

---

## 🧱 Microservices Architecture

```

Client
│
▼
API Gateway (Spring Cloud)
│
├── Auth Service (JWT, OAuth2)
├── Restaurant Service (Menu APIs)
├── Order Service (Order Placement)
└── Notification Service (Async via RabbitMQ)

````

All services are registered with **Eureka**, use **Spring Config Server**, and communicate securely via the **API Gateway**.

---

## 🚀 Getting Started

### 🛠️ Prerequisites

- Java 21+
- Maven 3.8+
- Docker (for RabbitMQ, Zipkin)
- Postman or curl (for testing)

### 🧪 Run Supporting Tools

Start RabbitMQ and Zipkin using Docker:

```bash
docker run -d --hostname rabbit --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
docker run -d -p 9411:9411 openzipkin/zipkin
````

### 🧭 Start Microservices (in order)

```bash
cd ../discovery-server && ./mvnw spring-boot:run
cd config-server && ./mvnw spring-boot:run
cd ../auth-service && ./mvnw spring-boot:run
cd ../api-gateway && ./mvnw spring-boot:run
cd ../restaurant-service && ./mvnw spring-boot:run
cd ../order-service && ./mvnw spring-boot:run
cd ../notification-service && ./mvnw spring-boot:run
```

---

## 🔐 Authentication Flow

### ✅ Login

```http
POST /auth/login
Content-Type: application/json

Admin :
{
  "name": "testuser1",
  "email": "test1@example.com",
  "password": "TestPass123"
}

Restaurant owner
{
  "name": "restaurent1",
  "email": "test2@example.com",
  "password": "TestPass123"
}

Customer
{
  "name": "customer1",
  "email": "test3@example.com",
  "password": "TestPass123"
}

```

Returns:

```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
    "tokenType": "Bearer",
    "username": "3",
    "role": "ROLE_CUSTOMER"
  }
}
```

Use the token for authorized requests via:

```http
Authorization: Bearer <token>
```

---

## 🍽️ Sample Workflow

### 📋 Get Menu

```http
GET /api/restaurant/menu
Authorization: Bearer <token>
```

---

### 🛒 Place an Order

```http
POST /api/order/place
Authorization: Bearer <token>

{
  "restaurantId": 1,
  "menuItemIds": [2, 3],
  "address": "123 Byte Street"
}
```

---

## 🐇 Asynchronous Messaging with RabbitMQ

* Order events are published to RabbitMQ
* Notification service listens to `order.notifications.queue` and processes order confirmation

Visit: [http://localhost:15672](http://localhost:15672)
Login: `guest` / `guest`

---

## 💥 Resilience4j Fault Tolerance

* Circuit breakers wrap all external service calls
* If a service (e.g., restaurant) fails, fallback methods respond gracefully

---

## 🔍 Distributed Tracing with Zipkin

Access Zipkin at: [http://localhost:9411](http://localhost:9411)

Traces show:

* Gateway → Order → Notification → Messaging chain

---

## 📁 Directory Structure

```bash
bytebites_restaurant_platform/
├── config-server/
├── discovery-server/
├── api-gateway/
├── auth-service/
├── restaurant-service/
├── order-service/
├── notification-service/
└── README.md
```

---

## 🧠 Learning Outcomes

* Deep understanding of Spring Cloud microservices ecosystem
* Implemented secure communication with JWT & OAuth2
* Mastered RabbitMQ for async service coordination
* Built production-ready fault-tolerant services with circuit breakers
* Integrated observability with Zipkin tracing

---

## 📬 Author

**Richmond Kwame Nyarko**
Project for Secure Microservices Lab — ByteBites Platform

---
