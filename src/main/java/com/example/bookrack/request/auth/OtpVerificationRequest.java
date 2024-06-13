package com.example.bookrack.request.auth;

import com.example.bookrack.utilities.AppUtility;
import lombok.Data;

@Data
public class OtpVerificationRequest {

    private String otpVerificationKey;

    private Integer otp;

    public void sanitizeInput(){
        setOtpVerificationKey(AppUtility.sanitizeInput(getOtpVerificationKey()));
    }
}
