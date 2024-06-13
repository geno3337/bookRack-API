package com.example.bookrack.repository;

import com.example.bookrack.entity.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActivityRepository extends JpaRepository<UserActivity,Integer> {
}
