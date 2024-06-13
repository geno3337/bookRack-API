package com.example.bookrack.service.config;

import com.example.bookrack.entity.AllowedApp;

public interface ConfigService {

    public AllowedApp getActiveApplicationInformation(String applicationKey);

    AllowedApp getAppInformation(int appId);
}
