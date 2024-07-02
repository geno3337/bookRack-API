package com.example.bookrack.repository;

import com.example.bookrack.entity.SocialMediaUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialMediaUserRepository extends JpaRepository<SocialMediaUser,Integer> {
    SocialMediaUser findBySocialEmailAndSource(String socialEmail, String source);
}
