package com.example.bookrack.service.user;

import com.example.bookrack.entity.Registration;
import com.example.bookrack.entity.User;
import com.example.bookrack.entity.UserActivity;

public interface UserService {

    public void saveUserRegistration(Registration registration);

    Registration getNotYetVerifiedRegistration(String otpVerificationKey);

    User createNewUser(Registration pendingRequest, String email);

    void saveUser(User newUser);

    UserActivity addNewUserActivity(int applicationId, Integer UserSessionId, int userInfoId, String action);

    User getActiveUserByEmail(String email);

    User getActiveUserByPasswordResetToken(String passwordResetToken);

    User getActiveUserById(int userId);
}
