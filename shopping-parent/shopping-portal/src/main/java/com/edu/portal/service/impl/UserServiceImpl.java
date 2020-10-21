package com.edu.portal.service.impl;

import com.edu.common.util.HttpClientUtil;
import com.edu.portal.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Value("${USER_BASE_URL}")
    private String USER_BASE_URL;
    @Value("${LOGINOUT_URL}")
    private String LOGINOUT_URL;
    @Override
    public void logout(String token) {
       HttpClientUtil.doGet(USER_BASE_URL + LOGINOUT_URL + token);
    }
}
