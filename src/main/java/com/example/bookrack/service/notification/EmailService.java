package com.example.bookrack.service.notification;

import com.example.bookrack.entity.EmailQueue;

import java.util.List;

public interface EmailService {

    EmailQueue saveEmailQueue(EmailQueue emailQueue);

    void addToEmailQueue(String template, String subject, Object templateData, String toEmailList);

    EmailQueue getNextPendingEmail();
}
