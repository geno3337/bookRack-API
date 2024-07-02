package com.example.bookrack.service.user;

import com.example.bookrack.entity.UserSession;
import org.springframework.stereotype.Service;


public interface UserSessionService {
    UserSession initiateUserSession(int userId, int appId, String loggedInUsing);

    UserSession activateNewSession(UserSession userSession, String refreshKey, String accessToken);

    UserSession getActiveSessionByAccessTokenIdentifier(String accessTokenIdentifier);

}
