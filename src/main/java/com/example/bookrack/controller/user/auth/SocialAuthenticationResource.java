package com.example.bookrack.controller.user.auth;

import com.example.bookrack.entity.AllowedApp;
import com.example.bookrack.request.auth.SocialLoginRequest;
import com.example.bookrack.response.auth.UserAuthenticationResponse;
import com.example.bookrack.service.HttpRequestService;
import com.example.bookrack.service.SocialLoginService;
import com.example.bookrack.service.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class SocialAuthenticationResource {

    @Autowired
    private HttpRequestService httpRequestService;

    @Autowired
    private SocialLoginService socialLoginService;

    @Autowired
    private ConfigService configService;

    @PostMapping("/socialLogin/{provider}")
    public ResponseEntity<UserAuthenticationResponse> doSocialAuthentication(@RequestBody SocialLoginRequest socialLoginRequest, @PathVariable String provider){
       socialLoginRequest.sanitizeInput();
        String token = socialLoginRequest.getToken();
        if(token.isEmpty()||token.isBlank()){
            throw new RuntimeException("invalid token");
        }
       if(provider.equals("google")){
            UserAuthenticationResponse response = socialLoginService.doGoogleLogin(socialLoginRequest);
            return new ResponseEntity<UserAuthenticationResponse>(response,HttpStatus.OK);
       } else if (provider.equals("github")) {
            UserAuthenticationResponse response = socialLoginService.doGithubLogin(socialLoginRequest);
            return new ResponseEntity<UserAuthenticationResponse>(response,HttpStatus.OK);
       }else {
           throw new RuntimeException("invalid login provider");
       }
    }

}
