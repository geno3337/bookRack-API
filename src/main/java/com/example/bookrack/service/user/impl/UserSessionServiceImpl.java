package com.example.bookrack.service.user.impl;

import com.example.bookrack.entity.UserSession;
import com.example.bookrack.repository.UserSessionRepository;
import com.example.bookrack.service.user.UserSessionService;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserSessionServiceImpl implements UserSessionService {

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Override
    public UserSession initiateUserSession(int userId, int appId, String loggedInUsing, int otp) {
        Date now = new Date();
        UserSession userSession = new UserSession();
        userSession.setUserId(userId);
        userSession.setApplicationId(appId);
        userSession.setLoggedInUsing(loggedInUsing);
        userSession.setOtp(otp);
        userSession.setOtpGeneratedAt(now);
        userSession.setIsOtpActivated(false);
        userSession.setOtpExpiredAt(DateUtils.addMinutes(now,15));
        return saveSession(userSession);
    }

    @Override
    public UserSession activateNewSession(UserSession userSession, String refreshKey, String accessToken) {
        Date now = new Date();
        userSession.setIsOtpActivated(true);
        userSession.setAccessToken(accessToken);
        userSession.setRefreshKey(refreshKey);
        userSession.setAccessTokenCreatedAt(now);
        userSession.setAccessTokenExpiresAt(DateUtils.addDays(now,30));
        userSession.setRefreshKeyCreatedAt(now);
        userSession.setRefreshKeyExpiredAt(DateUtils.addDays(now,24));
        userSession.setIsAccessTokenActive(true);
        userSession.setIsRefreshKeyActive(true);
        userSession.setLastActivityAt(now);
        return saveSession(userSession);
    }

    @Override
    public UserSession getActiveSessionByAccessTokenIdentifier(String accessTokenIdentifier) {
        return userSessionRepository.findByAccessTokenAndIsAccessTokenActive(accessTokenIdentifier,true);
    }


    private UserSession saveSession(UserSession userSession) {
        return userSessionRepository.save(userSession);
    }
}
