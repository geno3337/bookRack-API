package com.example.bookrack.request.auth;

import com.example.bookrack.utilities.AppUtility;
import lombok.Data;

@Data
public class VerifyForgetPasswordKeyRequest {

    private String applicationKey;

    private String passwordResetToken;

    public void sanitizeInput(){
        setApplicationKey(AppUtility.sanitizeInput(getApplicationKey()));
        setPasswordResetToken(AppUtility.sanitizeInput(getPasswordResetToken()));
    }
}
