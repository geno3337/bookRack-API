package com.example.bookrack.service;

import com.example.bookrack.request.auth.SocialLoginRequest;
import com.example.bookrack.response.auth.UserAuthenticationResponse;

public interface SocialLoginService {
    UserAuthenticationResponse doGoogleLogin(SocialLoginRequest socialLoginRequest);

    UserAuthenticationResponse doGithubLogin(SocialLoginRequest socialLoginRequest);
}
