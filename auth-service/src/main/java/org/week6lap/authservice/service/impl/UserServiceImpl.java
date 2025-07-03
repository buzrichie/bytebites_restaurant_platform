package org.week6lap.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.week6lap.authservice.dto.UserRecord;
import org.week6lap.authservice.dto.UserResponse;
import org.week6lap.authservice.exception.ResourceNotFoundException;
import org.week6lap.authservice.mapper.UserMapper;
import org.week6lap.authservice.model.User;
import org.week6lap.authservice.repository.UserRepository;
import org.week6lap.authservice.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Create and save a new user.
     * Cache will be cleared to reflect new data.
     */
    @Override
    @Transactional
    @CacheEvict(value = "users", allEntries = true) // Evict all cache entries
    public UserResponse createUser(UserRecord userRecord) {
        User user = userMapper.toEntity(userRecord);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    /**
     * Retrieve a user by ID and cache the result.
     */
    @Override
    @Cacheable(value = "users", key = "#id")
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found"));
        return userMapper.toResponse(user);
    }

    /**
     * Retrieve all users.
     * Cached using a fixed key "all".
     */
    @Override
    @Cacheable(value = "users", key = "'all'")
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Update an existing user and clear the relevant cache.
     */
    @Override
    @Transactional

    @Caching(evict = {
            @CacheEvict(value = "users", key = "#id"),
            @CacheEvict(value = "users", key = "'all'")
    })
    public UserResponse updateUser(Long id, UserRecord updatedRecord) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found"));

        existingUser.setEmail(updatedRecord.email());
        existingUser.setUsername(updatedRecord.username());
        existingUser.setRole(updatedRecord.role());
        existingUser.setPassword(passwordEncoder.encode(updatedRecord.password()));

        User updated = userRepository.save(existingUser);
        return userMapper.toResponse(updated);
    }

    /**
     * Delete a user by ID and clear related cache.
     */
    @Override
    @Transactional

    @Caching(evict = {
            @CacheEvict(value = "users", key = "#id"),
            @CacheEvict(value = "users", key = "'all'")
    })
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User with ID " + id + " not found");
        }
        userRepository.deleteById(id);
    }
}
