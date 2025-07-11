erDiagram
AppUser {
Long id PK
String email
String password
Set roles
}

    UserProfile {
        Long id PK
        String fullName
        String phone
        String address
    }

    Restaurant {
        Long id PK
        String name
        String location
        Long owner_id FK
    }

    MenuItem {
        Long id PK
        String name
        Double price
        String description
        Long restaurant_id FK
    }

    Order {
        Long id PK
        Long customer_id FK
        Long restaurant_id FK
        Date orderTime
        String status
    }

    OrderItem {
        Long id PK
        Integer quantity
        Long menuItem_id FK
        Long order_id FK
    }

    %% Relationships
    AppUser ||--o{ UserProfile : owns
    AppUser ||--o{ Restaurant : "is owner of"
    Restaurant ||--o{ MenuItem : "has"
    AppUser ||--o{ Order : "places"
    Restaurant ||--o{ Order : "receives"
    Order ||--o{ OrderItem : "contains"
    MenuItem ||--o{ OrderItem : "included in"
