package com.example.bookrack.repository.config;

import com.example.bookrack.entity.AllowedApp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllowedAppRepository extends JpaRepository<AllowedApp,Integer> {
    AllowedApp findByApplicationKeyAndIsActiveIsTrue(String applicationKey);

    AllowedApp findById(int appId);
}
