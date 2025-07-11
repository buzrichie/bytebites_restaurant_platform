# ByteBites Microservices Architecture Overview

This diagram illustrates the high-level architecture of the ByteBites secure microservices platform.

```mermaid
graph TD
    %% Define main actors/systems
    User[User/Client] -- HTTP Requests (REST) --> APIGateway

    %% Define Infrastructure Services
    subgraph Infrastructure_Services
        DiscoveryServer(Discovery Server - Eureka)
        ConfigServer(Config Server - Git-backed)
        EventBus(Event Bus - Kafka/RabbitMQ)
    end

    %% Define Core Services
    subgraph Core_Business_Microservices
        AuthService(Auth-Service)
        UserService(User-Service)
        RestaurantService(Restaurant-Service)
        OrderService(Order-Service)
        NotificationService(Notification-Service)
    end

    %% Define Databases
    subgraph Databases
        AuthDB[(Auth DB)]
        UserDB[(User DB)]
        RestaurantDB[(Restaurant DB)]
        OrderDB[(Order DB)]
        NotificationLogDB[(Notification Log DB)]
    end

    %% Service Registrations and Config Fetching
    AuthService -- Registers & Looks up --> DiscoveryServer
    UserService -- Registers & Looks up --> DiscoveryServer
    RestaurantService -- Registers & Looks up --> DiscoveryServer
    OrderService -- Registers & Looks up --> DiscoveryServer
    NotificationService -- Registers & Looks up --> DiscoveryServer
    APIGateway -- Looks up --> DiscoveryServer

    AuthService -- Fetches Config --> ConfigServer
    UserService -- Fetches Config --> ConfigServer
    RestaurantService -- Fetches Config --> ConfigServer
    OrderService -- Fetches Config --> ConfigServer
    NotificationService -- Fetches Config --> ConfigServer
    APIGateway -- Fetches Config --> ConfigServer

    %% Request Flow
    APIGateway -- (1) Validate JWT, Add Headers --> AuthService
    APIGateway -- (1) Validate JWT, Add Headers --> UserService
    APIGateway -- (1) Validate JWT, Add Headers --> RestaurantService
    APIGateway -- (1) Validate JWT, Add Headers --> OrderService
    APIGateway -- (1) Validate JWT, Add Headers --> NotificationService

    %% Authentication Flow
    User -- POST /auth/login --> APIGateway
    AuthService -- Generates JWT --> User
    AuthService -- Stores User Data --> AuthDB

    %% Business Service Interactions (via Gateway)
    User -- CRUD via API Gateway --> RestaurantService
    RestaurantService -- Accesses --> RestaurantDB

    User -- CRUD User Profile via API Gateway --> UserService
    UserService -- Manages User Data --> UserDB

    User -- Place Order via API Gateway --> OrderService
    OrderService -- Stores Order Data --> OrderDB

    %% Event-Driven Flow
    OrderService -- (2) Publishes OrderPlacedEvent --> EventBus
    EventBus -- (3) Consumes OrderPlacedEvent --> NotificationService
    EventBus -- (3) Consumes OrderPlacedEvent --> RestaurantService

    NotificationService -- Logs Notification --> NotificationLogDB

    %% Security & RBAC Enforcement
    APIGateway -. JWT Validation .-> AuthService
    UserService -. RBAC & Ownership .-> APIGateway
    RestaurantService -. RBAC & Ownership .-> APIGateway
    OrderService -. RBAC & Ownership .-> APIGateway

    style APIGateway fill:#add8e6,stroke:#333,stroke-width:2px,color:#000
    style DiscoveryServer fill:#f0e68c,stroke:#333,stroke-width:2px,color:#000
    style ConfigServer fill:#f0e68c,stroke:#333,stroke-width:2px,color:#000
    style EventBus fill:#f0e68c,stroke:#333,stroke-width:2px,color:#000

    style AuthService fill:#90ee90,stroke:#333,stroke-width:2px,color:#000
    style UserService fill:#90ee90,stroke:#333,stroke-width:2px,color:#000
    style RestaurantService fill:#90ee90,stroke:#333,stroke-width:2px,color:#000
    style OrderService fill:#90ee90,stroke:#333,stroke-width:2px,color:#000
    style NotificationService fill:#90ee90,stroke:#333,stroke-width:2px,color:#000

    style AuthDB fill:#d3d3d3,stroke:#333,stroke-width:1px,color:#000
    style UserDB fill:#d3d3d3,stroke:#333,stroke-width:1px,color:#000
    style RestaurantDB fill:#d3d3d3,stroke:#333,stroke-width:1px,color:#000
    style OrderDB fill:#d3d3d3,stroke:#333,stroke-width:1px,color:#000
    style NotificationLogDB fill:#d3d3d3,stroke:#333,stroke-width:1px,color:#000

