package com.example.bookrack.service.config.impl;

import com.example.bookrack.entity.AllowedApp;
import com.example.bookrack.repository.config.AllowedAppRepository;
import com.example.bookrack.service.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigServiceImpl implements ConfigService{

    @Autowired
    private AllowedAppRepository allowedAppRepository;

    @Override
    public AllowedApp getActiveApplicationInformation(String applicationKey) {
        return allowedAppRepository.findByApplicationKeyAndIsActiveIsTrue(applicationKey) ;
    }

    @Override
    public AllowedApp getAppInformation(int appId) {
        return allowedAppRepository.findById(appId);
    }
}
