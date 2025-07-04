package org.week6lap.authservice.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.week6lap.authservice.dto.auth.AuthLoginRequestDTO;
import org.week6lap.authservice.dto.auth.AuthRegisterRequestDTO;
import org.week6lap.authservice.dto.auth.AuthResponseDTO;
import org.week6lap.authservice.dto.user.UserResponse;
import org.week6lap.authservice.enums.UserRole;
import org.week6lap.authservice.exception.EmailAlreadyExist;
import org.week6lap.authservice.mapper.UserMapper;
import org.week6lap.authservice.model.User;
import org.week6lap.authservice.repository.UserRepository;
import org.week6lap.authservice.security.JwtTokenProvider;
import org.week6lap.authservice.service.AuthService;
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    /**
     * Authenticate user and return JWT token and role.
     */
    @Override
    @Transactional
    public AuthResponseDTO login(AuthLoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(), request.password() // record accessor
                )
        );

        String token = jwtTokenProvider.generateToken(authentication.getName(),  Map.of("email", authentication.getName(), "roles",  authentication.getAuthorities().iterator().next().getAuthority()));

        return new AuthResponseDTO(
                token,
                "Bearer",
                authentication.getName(),
                authentication.getAuthorities().iterator().next().getAuthority()
        );
    }

    /**
     * Register a new user with default CUSTOMER role.
     */
    @Override
    @Transactional
    public UserResponse register(AuthRegisterRequestDTO request) {
        checkForExistingData(request);
        User user = User.builder()
                .email(request.email())
                .username(request.name())
                .password(passwordEncoder.encode(request.password()))
                .role(UserRole.ROLE_CUSTOMER)
                .build();

        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(UserRole.ROLE_CUSTOMER);

        return userMapper.toResponse(userRepository.save(user));
    }

    /**
     * Check if the given email already exists in the system.
     */
    private void checkForExistingData(AuthRegisterRequestDTO request) {
        if (userRepository.existsByEmailIs(request.email())) { // record accessor
            throw new EmailAlreadyExist("Email already exists");
        }
    }
}
