package com.example.bookrack.controller.user.auth;

import com.example.bookrack.entity.AllowedApp;
import com.example.bookrack.entity.Registration;
import com.example.bookrack.entity.User;
import com.example.bookrack.entity.UserSession;
import com.example.bookrack.expection.RecordNotFoundException;
import com.example.bookrack.request.auth.*;
import com.example.bookrack.response.auth.UserAuthenticationResponse;
import com.example.bookrack.response.common.ApiResponse;
import com.example.bookrack.schedulers.EmailScheduler;
import com.example.bookrack.service.config.ConfigService;
import com.example.bookrack.service.user.UserService;
import com.example.bookrack.utilities.AppUtility;
import com.example.bookrack.utilities.EmailUtil;
import com.example.bookrack.utilities.JwtUtility;
import com.example.bookrack.utilities.UserSessionUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("/public")
public class userAuthenticationResource {

    @Autowired
    private ConfigService configService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserSessionUtil userSessionUtil;

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmailScheduler emailScheduler;

    @Autowired
    private EmailUtil emailUtil;

    @PostMapping("/Registration")
    public ResponseEntity<UserAuthenticationResponse> doUserRegistration(@RequestBody UserRegistrationRequest request){
        request.sanitizeInput();
        AllowedApp appInfo =configService.getActiveApplicationInformation(request.getApplicationKey());
        if( appInfo == null ){
            throw new RecordNotFoundException("Invalid  Request");
        }
        Registration newRegistration=new Registration();
        newRegistration.setAppId(appInfo.getId());
        newRegistration.setEmail(request.getEmail());
        newRegistration.setName(request.getName());
        newRegistration.setPassword(request.getPassword());
        newRegistration.setRequestIdentifier(DigestUtils.md5Hex(appInfo.getId()+new Date().toString()+Math.random()));
        newRegistration.setOtpGeneratedAt(new Date());
        newRegistration.setOtp(AppUtility.genterateRandomNumber(4,4));
        newRegistration.setOtpExpiresAt(DateUtils.addMinutes(new Date(),10));
        userService.saveUserRegistration(newRegistration);
        emailUtil.sendOtp(newRegistration.getEmail(),newRegistration.getOtp());
        UserAuthenticationResponse response = new UserAuthenticationResponse("success","Registration successfully", HttpStatus.OK.value());
        response.setIsVerificationRequired(true);
        response.setOtpVerificationKey(newRegistration.getRequestIdentifier());
        return new ResponseEntity<UserAuthenticationResponse>(response,HttpStatus.OK);

    }

    @PostMapping("/OtpVerification")
    public ResponseEntity<UserAuthenticationResponse> doOtpVerification(@RequestBody OtpVerificationRequest request){
        request.sanitizeInput();
        Registration pendingRequest=userService.getNotYetVerifiedRegistration(request.getOtpVerificationKey());
        if (pendingRequest == null){
            throw new RuntimeException("InvalidRequest");
        }
        AllowedApp appInfo = configService.getAppInformation(pendingRequest.getAppId());
        if (appInfo == null){
            throw new RuntimeException("invalid request");
        }
        if (!request.getOtp().equals(pendingRequest.getOtp())){
            throw new RuntimeException("Record mismatch exception");
        }
        if(pendingRequest.getOtpExpiresAt().before(new Date())){
            throw new RuntimeException("opt expired");
        }
        User user = userService.createNewUser(pendingRequest,"email");
        user.setIsEmailVerified(true);
        userService.saveUser(user);
        UserSession userSession = userSessionUtil.initiateUserVerification(user,appInfo,"email");
        userSessionUtil.activateNewSession(userSession);
        userService.addNewUserActivity(userSession.getApplicationId(),userSession.getId(),user.getId(),"login");
        UserAuthenticationResponse response = new UserAuthenticationResponse("success", "otp verified", 200);
        return new ResponseEntity<UserAuthenticationResponse>(response,HttpStatus.OK);
    }

    @PostMapping("/Login")
    public ResponseEntity<UserAuthenticationResponse> doUserLogin(@RequestBody UserLoginRequest userLoginRequest){
        userLoginRequest.sanitizeInput();
        AllowedApp appInfo = configService.getActiveApplicationInformation(userLoginRequest.getApplicationKey());
        if (appInfo == null){
            throw new RecordNotFoundException("invalid Request initiated");
        }
        User userInfo = userService.getActiveUserByEmail(userLoginRequest.getEmail());
        if(userInfo == null){
            throw new RecordNotFoundException("invalid email");
        }
        if (!userLoginRequest.getPassword().equals(userInfo.getPassword())){
            throw new RuntimeException("invalid credential");
        }
            UserSession initiateUserVerification = userSessionUtil.initiateUserVerification(userInfo,appInfo,"email");
            UserSession userSession = userSessionUtil.activateNewSession(initiateUserVerification);
            UserAuthenticationResponse response = new UserAuthenticationResponse("success","login successful",HttpStatus.OK.value());
            response.setAccessToken(jwtUtility.generateJWT(userSession.getAccessToken()));
            response.setRefreshkey(userSession.getRefreshKey());
            userService.addNewUserActivity(appInfo.getId(),userSession.getUserId(),userInfo.getId(),"login");
            return new ResponseEntity<UserAuthenticationResponse>(response,HttpStatus.OK);
    }

    @PostMapping("/resendOtp")
    public ResponseEntity<ApiResponse> resendOtp(@RequestBody ResendOtpRequest resendOtpRequest){
        resendOtpRequest.sanitizeInput();
        Registration pendingRequest = userService.getNotYetVerifiedRegistration(resendOtpRequest.getOtpVerificationKey());
        if(pendingRequest == null){
            throw new RuntimeException(resendOtpRequest.getOtpVerificationKey());
        }
        pendingRequest.setOtp(AppUtility.genterateRandomNumber(4,4));
        pendingRequest.setOtpGeneratedAt(new Date());
        pendingRequest.setOtpExpiresAt(DateUtils.addMinutes(new Date(),10));
        userService.saveUserRegistration(pendingRequest);
        emailUtil.sendOtp(pendingRequest.getEmail(),pendingRequest.getOtp());
        ApiResponse response = new ApiResponse("success","otp is resented to your email",200);
        return new ResponseEntity<ApiResponse>(response,HttpStatus.OK);
    }

    @PostMapping("/forgetPassword")
    public ResponseEntity<ApiResponse> initiateForgetPassword(@RequestBody InitiateForgetPasswordRequest initiateForgetPasswordRequest){
        initiateForgetPasswordRequest.sanitizeInput();
        AllowedApp appInfo = configService.getActiveApplicationInformation(initiateForgetPasswordRequest.getApplicationKey());
        if(appInfo == null){
            throw new RuntimeException("invalid request");
        }
        User userInfo = userService.getActiveUserByEmail(initiateForgetPasswordRequest.getEmail());
        if (userInfo == null){
            throw new RecordNotFoundException("User not found");
        }
        userInfo.setPasswordResetToken(DigestUtils.md2Hex(new Date().toString()+Math.random()+userInfo.getEmail()));
        userInfo.setPasswordResetTokenCreatedAt(new Date());
        userInfo.setPasswordResetTokenExpiredAt(DateUtils.addMinutes(new Date(),10));
        userService.saveUser(userInfo);
        emailUtil.sendResetPasswordUrl(userInfo.getEmail(),userInfo.getPasswordResetToken());
        userService.addNewUserActivity(appInfo.getId(),null,userInfo.getId(),"forget password");
        ApiResponse response = new ApiResponse("success","forget password initiated",200);
        return new  ResponseEntity<ApiResponse>(response,HttpStatus.OK);
    }

    @PostMapping("verifyForgetPasswordKey")
    public ResponseEntity<ApiResponse> verifyForgetPasswordKey(@RequestBody VerifyForgetPasswordKeyRequest request){
        request.sanitizeInput();
        AllowedApp appInfo = configService.getActiveApplicationInformation(request.getApplicationKey());
        if(appInfo == null){
            throw new RuntimeException("invalid request");
        }
        User userInfo = userService.getActiveUserByPasswordResetToken(request.getPasswordResetToken());
        if (userInfo == null){
            throw new RuntimeException("invalid passwordReset Token");
        }
        if (userInfo.getPasswordResetTokenExpiredAt().before(new Date())){
            throw new RuntimeException("PasswordReset token timeout");
        }
        ApiResponse response = new ApiResponse("success","verify success",200 );
        return new ResponseEntity<ApiResponse>(response,HttpStatus.OK);

    }

    @PostMapping("/updatePassword")
    public ResponseEntity<ApiResponse> updatePassword(@RequestBody UpdatePasswordRequest request){
        request.sanitizeInput();
        AllowedApp appInfo = configService.getActiveApplicationInformation(request.getApplicationKey());
        if(appInfo == null){
            throw new RuntimeException("invalid request");
        }
        User userInfo = userService.getActiveUserByPasswordResetToken(request.getPasswordResetToken());
        if (userInfo == null){
            throw new RuntimeException("invalid passwordReset Token");
        }
        if (userInfo.getPasswordResetTokenExpiredAt().before(new Date())){
            throw new RuntimeException("PasswordReset token timeout");
        }
        userInfo.setPassword(request.getNewPassword());
        userInfo.setPasswordResetToken(null);
        userInfo.setPasswordResetTokenCreatedAt(null);
        userInfo.setPasswordResetTokenExpiredAt(null);
        userService.saveUser(userInfo);
        ApiResponse response = new ApiResponse("success","update password success",200);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/testmethod")
    public String test(){
//        Gson gson = new Gson();
//        Type dataType = new TypeToken<Map<String, String>>(){}.getType();
//        Map<String, String> templateData = gson.fromJson("{name:'',otp:'1234'}", dataType);
//        return templateData.toString();
        return "test";
    }



}
