package org.week6lap.authservice.service;


import org.springframework.stereotype.Service;
import org.week6lap.authservice.dto.auth.AuthLoginRequestDTO;
import org.week6lap.authservice.dto.auth.AuthRegisterRequestDTO;
import org.week6lap.authservice.dto.auth.AuthResponseDTO;
import org.week6lap.authservice.dto.user.UserResponse;

@Service
public interface AuthService {
    AuthResponseDTO login(AuthLoginRequestDTO user);
    UserResponse register(AuthRegisterRequestDTO user);
}
