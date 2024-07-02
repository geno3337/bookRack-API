package com.example.bookrack.service;

import com.example.bookrack.entity.SocialMediaUser;

public interface SocialMediaUserService {
    SocialMediaUser getSocialMediaUser(String socialEmail, String google);

    void createNewUser(String socialEmail, int userId, String google);
}
