package com.dotdot.site.repository;

import com.dotdot.site.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // SELECT * FROM user WHERE username = 1?;
    User findAllByUsername(String username);
    // Optional<User> findAllByUsername(String Username);
}
