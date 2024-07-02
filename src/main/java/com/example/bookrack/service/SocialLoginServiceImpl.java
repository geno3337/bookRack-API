package com.example.bookrack.service;

import com.example.bookrack.entity.AllowedApp;
import com.example.bookrack.entity.SocialMediaUser;
import com.example.bookrack.entity.User;
import com.example.bookrack.entity.UserSession;
import com.example.bookrack.expection.RecordNotFoundException;
import com.example.bookrack.pojo.GitHubUser;
import com.example.bookrack.pojo.GoogleUser;
import com.example.bookrack.request.auth.SocialLoginRequest;
import com.example.bookrack.response.auth.UserAuthenticationResponse;
import com.example.bookrack.service.config.ConfigService;
import com.example.bookrack.service.user.UserService;
import com.example.bookrack.utilities.JwtUtility;
import com.example.bookrack.utilities.UserSessionUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SocialLoginServiceImpl implements SocialLoginService{

    @Autowired
    private HttpRequestService httpRequestService;

    @Autowired
    private SocialMediaUserService socialMediaUserService;

    @Autowired
    private UserSessionUtil userSessionUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private JwtUtility jwtUtility;

    @Value("${google.user-info-uri}")
    private String googleURL;
    @Value("${github.user-info-uri}")
    private String githubURl;

    public UserAuthenticationResponse doGoogleLogin(SocialLoginRequest socialLoginRequest){
        AllowedApp appInfo =configService.getActiveApplicationInformation(socialLoginRequest.getApplicationKey());
        if( appInfo == null ){
            throw new RecordNotFoundException("Invalid  Request");
        }
        String userJson = httpRequestService.getResponse(googleURL,socialLoginRequest.getToken());
        Gson gson=new Gson();
        GoogleUser googleUser=gson.fromJson(userJson, GoogleUser.class);
        if(googleUser.getEmail().isBlank()||googleUser.getEmail().isEmpty()){
            throw new RuntimeException("Email is empty");
        }
        SocialMediaUser socialUser = socialMediaUserService.getSocialMediaUser(googleUser.getEmail(),"google");
        User userInfo = new User();
        if (socialUser==null){
            userInfo = userService.getActiveUserByEmail(googleUser.getEmail());
            if (userInfo == null){
                // register using social login
                User newUser = userService.createNewSocialLoginUser(googleUser,"google");
                userInfo=userService.saveUser(newUser);
            }
            socialMediaUserService.createNewUser(googleUser.getEmail(),userInfo.getId(),"google");
        }else{
             userInfo = userService.getActiveUserById(socialUser.getUserId());
        }
        UserSession newSession = userSessionUtil.initiateUserVerification(userInfo,appInfo,"google");
        userSessionUtil.activateNewSession(newSession);
        userService.addNewUserActivity(appInfo.getId(),newSession.getId(),userInfo.getId(),"social Login");
        UserAuthenticationResponse response = new UserAuthenticationResponse("success","logged in success",200);
        response.setRefreshkey(newSession.getRefreshKey());
        response.setAccessToken(jwtUtility.generateJWT(newSession.getAccessToken()));
        return response;
    }

    @Override
    public UserAuthenticationResponse doGithubLogin(SocialLoginRequest socialLoginRequest) {
        AllowedApp appInfo =configService.getActiveApplicationInformation(socialLoginRequest.getApplicationKey());
        if( appInfo == null ){
            throw new RecordNotFoundException("Invalid  Request");
        }
        String userJson = httpRequestService.getResponse(githubURl,socialLoginRequest.getToken());
        Gson gson=new Gson();
        GitHubUser gitHubUser=gson.fromJson(userJson,GitHubUser.class);
        if(gitHubUser.getEmail().isBlank()||gitHubUser.getEmail().isEmpty()){
            throw new RuntimeException("Email is empty");
        }
        SocialMediaUser socialUser = socialMediaUserService.getSocialMediaUser(gitHubUser.getEmail(),"github");
        User userInfo = new User();
        if (socialUser==null){
            userInfo = userService.getActiveUserByEmail(gitHubUser.getEmail());
            if (userInfo == null){
                // register using social login
                User newUser = userService.createNewSocialLoginUser(gitHubUser,"github");
                userService.saveUser(newUser);
            }
            socialMediaUserService.createNewUser(gitHubUser.getEmail(),userInfo.getId(),"google");
        }else{
            userInfo = userService.getActiveUserById(socialUser.getUserId());
        }
        UserSession newSession = userSessionUtil.initiateUserVerification(userInfo,appInfo,"google");
        userSessionUtil.activateNewSession(newSession);
        userService.addNewUserActivity(appInfo.getId(),newSession.getId(),userInfo.getId(),"social Login");
        UserAuthenticationResponse response = new UserAuthenticationResponse("success","logged in success",200);
        response.setRefreshkey(newSession.getRefreshKey());
        response.setAccessToken(jwtUtility.generateJWT(newSession.getAccessToken()));
        return response;
    }


}
