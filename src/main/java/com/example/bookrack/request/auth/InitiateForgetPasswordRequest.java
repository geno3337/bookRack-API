package com.example.bookrack.request.auth;

import com.example.bookrack.utilities.AppUtility;
import lombok.Data;

@Data
public class InitiateForgetPasswordRequest {

    private String applicationKey;

    private String email;

    public void sanitizeInput(){
        setEmail(AppUtility.sanitizeInput(getEmail()));
        setApplicationKey(AppUtility.sanitizeInput(getApplicationKey()));
    }
}
