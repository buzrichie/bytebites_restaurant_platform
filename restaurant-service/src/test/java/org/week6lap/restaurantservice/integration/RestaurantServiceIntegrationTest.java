package org.week6lap.restaurantservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.week6lap.restaurantservice.dto.menu.MenuItemRecord;
import org.week6lap.restaurantservice.dto.restaurant.RestaurantRecord;
import org.week6lap.restaurantservice.model.MenuItem;
import org.week6lap.restaurantservice.model.Restaurant;
import org.week6lap.restaurantservice.repository.MenuItemRepository;
import org.week6lap.restaurantservice.repository.RestaurantRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${internal.secret}")
    private String internalSecret;

    private Restaurant testRestaurant;

    @BeforeEach
    void setUp() {
        menuItemRepository.deleteAll();
        restaurantRepository.deleteAll();

        testRestaurant = restaurantRepository.save(Restaurant.builder()
                .name("Test Kitchen")
                .address("123 Food Street")
                .phone("+233500000000")
                .ownerId(1L)
                .build());

        menuItemRepository.saveAll(List.of(
                MenuItem.builder()
                        .name("Jollof Rice")
                        .description("Delicious jollof with chicken")
                        .price(BigDecimal.valueOf(25.50))
                        .restaurant(testRestaurant)
                        .build(),
                MenuItem.builder()
                        .name("Waakye Special")
                        .description("Waakye with all toppings")
                        .price(BigDecimal.valueOf(30.00))
                        .restaurant(testRestaurant)
                        .build()
        ));
    }

    @Test
    void shouldReturnAllRestaurants() throws Exception {
        mockMvc.perform(get("/api/v1/restaurants")
                        .header("X-USER-ID", "1")
                        .header("X-USER-ROLE", "ROLE_RESTAURANT_OWNER")
                        .header("X-Internal-Auth", internalSecret))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("All restaurants fetched"));
    }

    @Test
    void shouldReturnRestaurantById() throws Exception {
        mockMvc.perform(get("/api/v1/restaurants/" + testRestaurant.getId())
                        .header("X-USER-ID", "1")
                        .header("X-USER-ROLE", "ROLE_RESTAURANT_OWNER")
                        .header("X-Internal-Auth", internalSecret))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Restaurant fetched"));
    }

    @Test
    void shouldGetMenuItemsForRestaurant() throws Exception {
        mockMvc.perform(get("/api/v1/menu-items/restaurant/" + testRestaurant.getId())
                        .header("X-USER-ID", "1")
                        .header("X-USER-ROLE", "ROLE_RESTAURANT_OWNER")
                        .header("X-Internal-Auth", internalSecret))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Menu items loaded"));
    }

    @Test
    void shouldCreateRestaurant() throws Exception {
        RestaurantRecord record = new RestaurantRecord("New Kitchen", "456 Tasty Road", "+233511111111", 1L,null);

        mockMvc.perform(post("/api/v1/restaurants")
                        .header("X-USER-ID", "1")
                        .header("X-USER-ROLE", "ROLE_RESTAURANT_OWNER")
                        .header("X-Internal-Auth", internalSecret)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Restaurant created successfully"));
    }

    @Test
    void shouldCreateMenuItem() throws Exception {
        MenuItemRecord record = new MenuItemRecord("Kelewele", "Spicy fried plantains", BigDecimal.valueOf(10));

        mockMvc.perform(post("/api/v1/menu-items/restaurant/" + testRestaurant.getId())
                        .header("X-USER-ID", "1")
                        .header("X-USER-ROLE", "ROLE_RESTAURANT_OWNER")
                        .header("X-Internal-Auth", internalSecret)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Menu item created successfully"));
    }
}
