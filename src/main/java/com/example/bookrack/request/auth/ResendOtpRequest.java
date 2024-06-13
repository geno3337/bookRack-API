package com.example.bookrack.request.auth;

import com.example.bookrack.utilities.AppUtility;
import lombok.Data;

@Data
public class ResendOtpRequest {

    private String otpVerificationKey;

    public void sanitizeInput() {
        setOtpVerificationKey(AppUtility.sanitizeInput(getOtpVerificationKey()));
    }
}
