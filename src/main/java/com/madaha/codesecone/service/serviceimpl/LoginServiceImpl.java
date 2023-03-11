package com.madaha.codesecone.service.serviceimpl;

import com.madaha.codesecone.service.LoginService;
import com.madaha.codesecone.entity.User;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    /**
     * 登录验证模块：
     * 1、通过硬编码 账密实现；
     * 2、通过查询数据库的 用户名和密码 进行验证，实现认证功能；
     * 注意：此处使用的是硬编码形式，并未进行数据库的 查询 操作。
     */
    @Override
    public User loginService(String uname, String password) {

        // 硬编码，密码明文写死
        String user = "admin";
        String pass = "admin";

        // 认证
        if (true){

        }else{

        }

        return null;
    }


    /**
     * 用户注册功能，此功能暂不实现
     * 待后续再研究，先解决最主要的功能模块
     */
    @Override
    public User registService(User user) {
        return null;
    }
}
