package org.week6lap.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class GatewayRoutesConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/api/v1/auth/**")
                        .uri("lb://AUTH-SERVICE"))
                .route("auth-service", r -> r.path("/api/v1/users/**")
                        .uri("lb://AUTH-SERVICE"))
                .route("restaurant-service", r -> r.path("/api/v1/restaurants/**", "/api/v1/menu-items/**")
                        .uri("lb://RESTAURANT-SERVICE"))
                .route("order-service", r -> r.path("/api/v1/orders/**")
                        .uri("lb://ORDER-SERVICE"))
                .route("notification-service", r -> r.path("/api/v1/notifications/**")
                        .uri("lb://NOTIFICATION-SERVICE"))
                .build();
    }
}
