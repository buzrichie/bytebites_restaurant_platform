package org.week6lap.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.week6lap.authservice.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
