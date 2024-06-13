package com.example.bookrack.response.auth;

import com.example.bookrack.response.common.ApiResponse;
import lombok.Data;

@Data
public class UserAuthenticationResponse extends ApiResponse {

    private String refreshkey;

    private String accessToken;

    private String otpVerificationKey;

    private Boolean  isVerificationRequired;

    private String userId;

    private String userType;

    public UserAuthenticationResponse(String status, String message, Integer statusCode) {
        super(status, message, statusCode);
    }
}
