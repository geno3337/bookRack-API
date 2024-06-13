package com.example.bookrack.repository;

import com.example.bookrack.entity.EmailQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailQueueRepository extends JpaRepository<EmailQueue,Integer> {
    EmailQueue findFirstByStatusOrderByQueuedAtAsc(String pending);
}
