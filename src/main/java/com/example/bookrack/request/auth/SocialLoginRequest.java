package com.example.bookrack.request.auth;

import com.example.bookrack.utilities.AppUtility;
import lombok.Data;

@Data
public class SocialLoginRequest {

    private String applicationKey;

    private String token;

    private String attemptForm;

    public void sanitizeInput() {
        setToken(AppUtility.sanitizeInput(getToken()));
        setApplicationKey(AppUtility.sanitizeInput(getApplicationKey()));
        setAttemptForm(AppUtility.sanitizeInput(getAttemptForm()));
        if (getAttemptForm() == null || getAttemptForm().isEmpty()){
            setAttemptForm("login");
        }
    }
}
