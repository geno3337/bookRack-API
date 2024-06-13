package com.example.bookrack.repository;

import com.example.bookrack.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSessionRepository extends JpaRepository<UserSession,Integer> {

    UserSession findByAccessTokenAndIsAccessTokenActive(String accessTokenIdentifier, boolean isAccessTokenActive);
}
