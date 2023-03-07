package com.madaha.codesecone.Service.ServiceImpl;

import com.madaha.codesecone.Service.LoginService;
import com.madaha.codesecone.entity.User;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Override
    public User loginService(String uname, String password) {

        //验证码验证 CaptchaUtil


        //账号密码验证，并创建 JWT Token



        //返回视图
        return null;
    }


    @Override
    public User registService(User user) {


        return null;
    }
}
