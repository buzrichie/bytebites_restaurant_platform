package org.week6lap.restaurantservice.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.week6lap.restaurantservice.model.MenuItem;
import org.week6lap.restaurantservice.model.Restaurant;
import org.week6lap.restaurantservice.repository.MenuItemRepository;
import org.week6lap.restaurantservice.repository.RestaurantRepository;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Testcontainers
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class MenuItemServiceIntegrationTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Value("${internal.secret}")
    private String internalSecret;

    private Long restaurantId;
    private Long ownerId = 1L;

    @BeforeEach
    void setUp() {
        // Clean and seed the database
        menuItemRepository.deleteAll();
        restaurantRepository.deleteAll();

        Restaurant restaurant = Restaurant.builder()
                .name("Spicy Bites")
                .address("456 Flavor Road")
                .phone("+233511223344")
                .ownerId(ownerId)
                .build();

        restaurant = restaurantRepository.save(restaurant);
        this.restaurantId = restaurant.getId();

        MenuItem item = MenuItem.builder()
                .name("Fufu & Goat Soup")
                .description("Traditional Ghanaian dish")
                .price(BigDecimal.valueOf(35.00))
                .restaurant(restaurant)
                .build();

        menuItemRepository.save(item);
    }

    @Test
    void shouldGetMenuItemsByRestaurant() throws Exception {
        mockMvc.perform(get("/api/v1/menu-items/restaurant/" + restaurantId)
                        .header("X-USER-ID", ownerId)
                        .header("X-USER-ROLE", "ROLE_RESTAURANT_OWNER")
                        .header("X-Internal-Auth", internalSecret)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Menu items loaded"))
                .andExpect(jsonPath("$.data[0].name").value("Fufu & Goat Soup"));
    }

    @Test
    void shouldCreateMenuItem() throws Exception {
        String payload = """
            {
                "name": "Kelewele",
                "description": "Spicy fried plantains",
                "price": 15.00
            }
        """;

        mockMvc.perform(post("/api/v1/menu-items/restaurant/" + restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                        .header("X-USER-ID", ownerId.toString())
                        .header("X-USER-ROLE", "ROLE_RESTAURANT_OWNER")
                        .header("X-Internal-Auth", internalSecret)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Menu item created successfully"))
                .andExpect(jsonPath("$.data.name").value("Kelewele"));
    }
}
