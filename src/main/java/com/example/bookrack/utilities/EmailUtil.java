package com.example.bookrack.utilities;

import com.example.bookrack.pojo.EmailOtpTemplate;
import com.example.bookrack.service.notification.EmailService;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailUtil {

    @Autowired
    private EmailService emailService;

    public  void sendOtp(String email, Integer otp) {
        String[] emailList = AppUtility.extractCommaSeparatedData(email);
        EmailOtpTemplate templateData = new EmailOtpTemplate();
        templateData.setData(" "+" ",otp.toString());
        emailService.addToEmailQueue("loginotp.ftl","login otp verification",templateData,email);
    }

    public void sendResetPasswordUrl(String email, String passwordResetToken) {
    }
}
