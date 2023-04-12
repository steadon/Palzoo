package com.sipc.chatserver.service.impl;

import com.sipc.chatserver.exception.BusinessException;
import com.sipc.chatserver.pojo.domain.User;
import com.sipc.chatserver.service.feign.LoginServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class FeignImpl {
    private final LoginServer loginServer;

    @Autowired
    public FeignImpl(LoginServer loginServer) {
        this.loginServer = loginServer;

    }

    @Cacheable(cacheNames = "user", key = "#openid")
    public User getUser(String openid) {
        if (openid == null) throw new BusinessException("openid为null");
        return loginServer.getUser(openid);
    }
}
