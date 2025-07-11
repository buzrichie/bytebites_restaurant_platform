package org.week6lap.authservice.security.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.week6lap.authservice.security.CustomUserDetailsService;
import org.week6lap.authservice.security.JwtTokenProvider;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        System.out.println("header: " + header);

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (jwtTokenProvider.validateToken(token)) {
                String userId = jwtTokenProvider.getSubjectFromToken(token);
//                UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId);
//                UsernamePasswordAuthenticationToken authentication =
//                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                List<String> roles = jwtTokenProvider.getRolesFromToken(token); // custom method

                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set to Spring Security context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }

        filterChain.doFilter(request, response);
    }
}
