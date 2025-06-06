package com.example.bcryptauthservice.Repository;

import com.example.bcryptauthservice.Models.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Integer> {
    Optional<UserSession> findByTokenAndId(String token, int id);
}
