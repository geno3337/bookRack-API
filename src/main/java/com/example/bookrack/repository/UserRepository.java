package com.example.bookrack.repository;

import com.example.bookrack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findByEmail(String username);

    User findByEmailAndIsActiveAndIsEmailVerified(String email,boolean isActive,boolean isVerified);

    User findByPasswordResetTokenAndIsActiveAndIsEmailVerified(String passwordResetToken,boolean isActive,boolean isVerified);

    User findByIdAndIsActive(int userId, boolean isActive);
}
