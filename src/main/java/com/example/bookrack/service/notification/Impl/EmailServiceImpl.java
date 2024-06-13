package com.example.bookrack.service.notification.Impl;

import com.example.bookrack.entity.EmailQueue;
import com.example.bookrack.repository.EmailQueueRepository;
import com.example.bookrack.service.notification.EmailService;
import com.example.bookrack.utilities.AppUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${bookrack.emailqueue.fromemail}")
    private String fromEmail;

    @Autowired
    private EmailQueueRepository emailQueueRepository;

    @Override
    public EmailQueue saveEmailQueue(EmailQueue emailQueue) {
        return emailQueueRepository.save(emailQueue);
    }

    @Override
    public void addToEmailQueue(String template, String subject, Object templateData, String toEmailList) {
        EmailQueue emailQueue = new EmailQueue();
        emailQueue.setToEmail(toEmailList);
        emailQueue.setFromEmail(fromEmail);
        emailQueue.setSubject(subject);
        emailQueue.setTemplateName(template);
        emailQueue.setTemplateData(AppUtility.convertToJSON(templateData));
        emailQueue.setStatus("pending");
        emailQueue.setNoOfAttempt(0);
        emailQueue.setQueuedAt(new Date());
        saveEmailQueue(emailQueue);
    }

    @Override
    public EmailQueue getNextPendingEmail() {
        return emailQueueRepository.findFirstByStatusOrderByQueuedAtAsc("pending");
    }


}
