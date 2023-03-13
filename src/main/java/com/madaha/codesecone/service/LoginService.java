package com.madaha.codesecone.service;

import com.madaha.codesecone.entity.User;

public interface LoginService {

    /**
     * 用户名、密码 登录认证
     * @param username
     * @param password
     * @return
     */
    boolean loginService(String username, String password);


    /**
     * 注册业务逻辑
     * @param user
     * @return
     *
     * 注意：要注册的User对象，属性中主键uid要为空，若uid不为空可能会覆盖已存在的user
     */
    User registService(User user);

}
