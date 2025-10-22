package com.abhay.userservice.repository;

import com.abhay.userservice.domain.Confirmation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationRepository extends JpaRepository<Confirmation,Long> {
    Confirmation findByToken(String token);
}
