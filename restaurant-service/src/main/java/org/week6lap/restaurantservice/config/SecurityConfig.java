package org.week6lap.restaurantservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.week6lap.restaurantservice.security.filter.InternalRequestValidatorFilter;
import org.week6lap.restaurantservice.security.filter.UserContextAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final InternalRequestValidatorFilter internalRequestValidatorFilter;
    private final UserContextAuthenticationFilter userContextAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/swagger/**", "/v3/api-docs/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,
                                "/api/v1/restaurants/**",
                                "/api/v1/menu-items/restaurant/**"
                        ).hasAnyRole("CUSTOMER", "RESTAURANT_OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/restaurants").hasRole("RESTAURANT_OWNER")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/restaurants/**").hasRole("RESTAURANT_OWNER")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/restaurants/**").hasRole("RESTAURANT_OWNER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/menu-items/restaurant/**").hasRole("RESTAURANT_OWNER")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/menu-items/**").hasRole("RESTAURANT_OWNER")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/menu-items/**").hasRole("RESTAURANT_OWNER")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(internalRequestValidatorFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(userContextAuthenticationFilter, InternalRequestValidatorFilter.class);

        return http.build();
    }
}

