package com.example.bookrack.request.auth;

import lombok.Data;

@Data
public class UserRegistrationRequest {

    private String applicationKey;

    private String name;

    private String email;

    private String password;

    private String conformPassword;

    public void sanitizeInput() {

    }
}
