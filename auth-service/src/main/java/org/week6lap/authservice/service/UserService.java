package org.week6lap.authservice.service;

import org.week6lap.authservice.dto.user.UserRecord;
import org.week6lap.authservice.dto.user.UserResponse;

import java.util.List;

public interface UserService {

    /**
     * Create and save a new user in the system.
     *
     * @param userRecord validated user input (DTO)
     * @return UserResponse DTO without password
     */
    UserResponse createUser(UserRecord userRecord);

    /**
     * Fetch a user by their unique ID.
     *
     * @param id User ID
     * @return UserResponse if found
     */
    UserResponse getUserById(Long id);

    /**
     * Fetch all users in the system.
     *
     * @return list of UserResponse DTOs
     */
    List<UserResponse> getAllUsers();

    /**
     * Update an existing user.
     *
     * @param id ID of user to update
     * @param userRecord new data to apply
     * @return updated UserResponse DTO
     */
    UserResponse updateUser(Long id, UserRecord userRecord);

    /**
     * Delete a user by their ID.
     *
     * @param id ID of user to delete
     */
    void deleteUser(Long id);
}
