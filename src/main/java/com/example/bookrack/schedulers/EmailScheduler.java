package com.example.bookrack.schedulers;

import com.example.bookrack.entity.EmailQueue;
import com.example.bookrack.service.notification.EmailService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;


import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ConditionalOnProperty(prefix = "schedulers.emailqueue", name = "enabled", havingValue = "true")
@Component
public class EmailScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    @Qualifier("notificationTemplateConfigBean")
    private Configuration templateConfig;

    public static final String LOGINOTP_TEMPLATE = "loginotp.ftl";


    @Scheduled(fixedDelay = 1000, initialDelay = 15000)
    public void processSendingEmail(){
        EmailQueue emailQueue = emailService.getNextPendingEmail();
        if (emailQueue != null ){
            emailQueue = updateAsProcess(emailQueue);
            initiateEmailSendingProcess(emailQueue);
        }

    }

    private void initiateEmailSendingProcess(EmailQueue emailQueue) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(() -> {
            try {
                sendEmail(emailQueue);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (TemplateException e) {
                throw new RuntimeException(e);
            }
        });
        executorService.shutdown();
    }


    private void sendEmail(EmailQueue emailQueue) throws MessagingException, IOException, TemplateException {
//        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message,MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            System.out.println("email/"+emailQueue.getTemplateName());
            Template template = templateConfig.getTemplate("email/"+emailQueue.getTemplateName());
            Map<String,String> templateDataSet = extractTemplateDataSet(emailQueue.getTemplateData());
            System.out.println(templateDataSet);
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template,templateDataSet);
            mimeMessageHelper.setTo(emailQueue.getToEmail());
            mimeMessageHelper.setFrom(emailQueue.getFromEmail());
            mimeMessageHelper.setSubject(emailQueue.getSubject());
            mimeMessageHelper.setText(html,true);
            if (emailQueue.getBccEmail() != null){
                mimeMessageHelper.setBcc(emailQueue.getBccEmail());
            }
            emailSender.send(message);
            updateAsSending(emailQueue);
//        }
//        catch (Exception e){
//            updateAsFailed(emailQueue,e.getMessage());
//        }
    }

    private void updateAsFailed(EmailQueue emailQueue, String description) {
        emailQueue.setStatus("Failed");
        emailQueue.setNoOfAttempt(emailQueue.getNoOfAttempt()+1);
//        emailQueue.setDescription(description);
        emailService.saveEmailQueue(emailQueue);
    }

    private void updateAsSending(EmailQueue emailQueue) {
        emailQueue.setStatus("Completed");
        emailQueue.setNoOfAttempt(emailQueue.getNoOfAttempt()+1);
        emailService.saveEmailQueue(emailQueue);
    }

    private Map<String, String> extractTemplateDataSet(String dataSet) {
        Gson gson = new Gson();
        Type dataType = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> templateData = gson.fromJson(dataSet, dataType);
        return templateData;
    }

    private EmailQueue updateAsProcess(EmailQueue emailQueue) {
        emailQueue.setStatus("process");
        emailQueue.setProcessedAt(new Date());
        return emailService.saveEmailQueue(emailQueue);
    }



}
