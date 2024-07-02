package com.example.bookrack.service.user;

import com.example.bookrack.entity.Registration;
import com.example.bookrack.entity.User;
import com.example.bookrack.entity.UserActivity;
import com.example.bookrack.pojo.GitHubUser;
import com.example.bookrack.pojo.GoogleUser;

public interface UserService {

    public void saveUserRegistration(Registration registration);

    Registration getNotYetVerifiedRegistration(String otpVerificationKey);

    User createNewUser(Registration pendingRequest, String email);

    User saveUser(User newUser);

    UserActivity addNewUserActivity(int applicationId, Integer UserSessionId, int userInfoId, String action);

    User getActiveUserByEmail(String email);

    User getActiveUserByPasswordResetToken(String passwordResetToken);

    User getActiveUserById(int userId);

    User createNewSocialLoginUser(GoogleUser googleUser, String source);

    User createNewSocialLoginUser(GitHubUser gitHubUser, String source);
}
