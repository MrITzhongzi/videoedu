package com.itzhongzi.videoedu.service;

import com.itzhongzi.videoedu.domain.User;

/**
 * 用户业务接口
 */
public interface UserService {

    public User SaveWeChatUser(String code);
    public User SaveWeChatUserTest();
}
