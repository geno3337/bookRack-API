package com.example.bookrack.service.user.impl;

import com.example.bookrack.entity.Registration;
import com.example.bookrack.entity.User;
import com.example.bookrack.entity.UserActivity;
import com.example.bookrack.repository.RegistrationRepository;
import com.example.bookrack.repository.UserActivityRepository;
import com.example.bookrack.repository.UserRepository;
import com.example.bookrack.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserActivityRepository userActivityRepository;

    @Override
    public void saveUserRegistration(Registration registration) {
        registrationRepository.save(registration);
    }

    @Override
    public Registration getNotYetVerifiedRegistration(String otpVerificationKey) {
        return registrationRepository.findByRequestIdentifier(otpVerificationKey);
    }

    @Override
    public User createNewUser(Registration pendingRequest, String signedInUsing) {
        User newUser = new User();
        newUser.setEmail(pendingRequest.getEmail());
        newUser.setName(pendingRequest.getName());
        newUser.setPassword(pendingRequest.getPassword());
        newUser.setRole("user");
        newUser.setIsActive(true);
        newUser.setSignedInUsing(signedInUsing);
        return newUser;
    }

    @Override
    public void saveUser(User newUser) {
        userRepository.save(newUser);
    }

    @Override
    public UserActivity addNewUserActivity(int applicationId, Integer sessionId, int userId, String action) {
        UserActivity newActivity = new UserActivity();
        newActivity.setApplicationId(applicationId);
        newActivity.setSessionId(sessionId);
        newActivity.setUserId(userId);
        newActivity.setAction(action);
        return userActivityRepository.save(newActivity);
    }

    @Override
    public User getActiveUserByEmail(String email) {
        return userRepository.findByEmailAndIsActiveAndIsEmailVerified(email,true,true);
    }

    @Override
    public User getActiveUserByPasswordResetToken(String passwordResetToken) {
        return userRepository.findByPasswordResetTokenAndIsActiveAndIsEmailVerified(passwordResetToken,true,true);
    }

    @Override
    public User getActiveUserById(int userId) {
        return userRepository.findByIdAndIsActive(userId,true);
    }
}
