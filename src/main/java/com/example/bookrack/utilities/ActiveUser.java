package com.example.bookrack.utilities;

import com.example.bookrack.entity.User;
import com.example.bookrack.entity.UserSession;
import lombok.Data;
import org.springframework.security.core.context.SecurityContextHolder;

@Data
public class ActiveUser {

    private User userInfo;

    private UserSession userSessionInfo;

    public ActiveUser() {
        try {
            User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserSession sessionDetails = (UserSession) SecurityContextHolder.getContext().getAuthentication().getCredentials();
            setActiveUser(userDetails, sessionDetails);
        } catch (NullPointerException e) {
        }
    }

    private void setActiveUser(User userDetails, UserSession userSession) {
        setUserInfo(userDetails);
        setUserSessionInfo(userSession);
    }
}
