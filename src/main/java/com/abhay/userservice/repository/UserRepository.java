package com.abhay.userservice.repository;

import com.abhay.userservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmailIgnoreCase(String email);
    Boolean  existsByEmail(String email);
}
