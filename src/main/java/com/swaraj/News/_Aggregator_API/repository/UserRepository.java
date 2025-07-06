package com.swaraj.News._Aggregator_API.repository;

import com.swaraj.News._Aggregator_API.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
