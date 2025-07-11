package org.week6lap.restaurantservice.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class RestaurantServiceIntegrationTest {

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

    @BeforeEach
    void setUp() {
        // Clean the database to ensure a fresh start
        menuItemRepository.deleteAll();
        restaurantRepository.deleteAll();

        // Create a Restaurant entity manually
        Restaurant restaurant = Restaurant.builder()
                .name("Test Kitchen")
                .address("123 Food Street")
                .phone("+233500000000")
                .ownerId(1L)
                .build();
        restaurant = restaurantRepository.save(restaurant);

        // Add some menu items
        MenuItem item1 = MenuItem.builder()
                .name("Jollof Rice")
                .description("Delicious jollof with chicken")
                .price(BigDecimal.valueOf(25.50))
                .restaurant(restaurant)
                .build();

        MenuItem item2 = MenuItem.builder()
                .name("Waakye Special")
                .description("Waakye with all toppings")
                .price(BigDecimal.valueOf(30.00))
                .restaurant(restaurant)
                .build();

        menuItemRepository.saveAll(List.of(item1, item2));
    }

    @Test
    void shouldReturnAllRestaurants_WhenRequested() throws Exception {
        mockMvc.perform(get("/api/v1/restaurants")
                        .header("X-USER-ID", "1")
                        .header("X-USER-ROLE", "ROLE_RESTAURANT_OWNER")
                        .header("X-Internal-Auth", internalSecret)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("All restaurants fetched"));
    }
}
