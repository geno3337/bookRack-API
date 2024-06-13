package com.example.bookrack.request.auth;

import com.example.bookrack.utilities.AppUtility;
import lombok.Data;

@Data
public class UpdatePasswordRequest {

    private String applicationKey;

    private String passwordResetToken;

    private String newPassword;

    private String conformPassword;

    public void sanitizeInput(){
        setApplicationKey(AppUtility.sanitizeInput(getApplicationKey()));
        setPasswordResetToken(AppUtility.sanitizeInput(getPasswordResetToken()));
        setNewPassword(AppUtility.sanitizeInput(getNewPassword()));
        setConformPassword(AppUtility.sanitizeInput(getConformPassword()));
    }
}
