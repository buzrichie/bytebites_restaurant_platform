package org.week6lap.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.week6lap.authservice.model.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmailIs(String email);
    Optional<User> findByEmail(String email);
}
