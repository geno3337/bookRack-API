package com.example.bookrack.repository;

import com.example.bookrack.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration,Integer> {
     Registration findByRequestIdentifier(String otpVerificationKey) ;
}
