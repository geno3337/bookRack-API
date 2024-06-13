package com.example.bookrack.utilities;

import com.example.bookrack.entity.AllowedApp;
import com.example.bookrack.entity.User;
import com.example.bookrack.entity.UserSession;
import com.example.bookrack.service.user.UserSessionService;
import lombok.experimental.UtilityClass;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class UserSessionUtil {

    @Autowired
    private UserSessionService userSessionService;

    public UserSession initiateUserVerification(User user, AllowedApp appInfo, String loggedInUsing) {
        int otp = AppUtility.genterateRandomNumber(4,4);
        return userSessionService.initiateUserSession(user.getId(),appInfo.getId(),loggedInUsing,otp) ;
    }

    public UserSession activateNewSession(UserSession userSession) {
        String refreshKey = generateRefreshKey(userSession);
        String accessToken = generateAccessToken(refreshKey);
        return userSessionService.activateNewSession(userSession,refreshKey,accessToken);
    }

    private String generateAccessToken(String refreshKey) {
        return DigestUtils.md2Hex(refreshKey+new Date().toString()+Math.random());
    }

    private String generateRefreshKey(UserSession userSession) {
        return DigestUtils.md2Hex(String.valueOf(userSession.getId()+ userSession.getUserId()+Math.random()));
    }
}
