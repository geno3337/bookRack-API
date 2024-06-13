package com.example.bookrack.request.auth;

import com.example.bookrack.utilities.AppUtility;
import lombok.Data;

@Data
public class UserLoginRequest {

    private String applicationKey;

    private String email;

    private String password;

    public void sanitizeInput(){
        setApplicationKey(AppUtility.sanitizeInput(applicationKey));
        setEmail(AppUtility.sanitizeInput(email));
        setPassword(AppUtility.sanitizeInput(password));
    }

}
