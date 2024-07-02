package com.example.bookrack.service;

import com.example.bookrack.entity.SocialMediaUser;
import com.example.bookrack.repository.SocialMediaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SocialMediaUserServiceImpl implements SocialMediaUserService{

    @Autowired
    private SocialMediaUserRepository socialMediaUserRepository;

    @Override
    public SocialMediaUser getSocialMediaUser(String socialEmail, String source) {
        return socialMediaUserRepository.findBySocialEmailAndSource(socialEmail,source);
    }

    @Override
    public void createNewUser(String socialEmail, int userId, String source) {
        SocialMediaUser socialUser = new SocialMediaUser();
        socialUser.setSocialEmail(socialEmail);
        socialUser.setUserId(userId);
        socialUser.setSource(source);
        socialMediaUserRepository.save(socialUser);
    }
}
