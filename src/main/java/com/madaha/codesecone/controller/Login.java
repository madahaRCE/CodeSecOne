package com.madaha.codesecone.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//这里别用@RestController,用了你的请求会给你返回个json。这里要的是路由
@Controller
public class Login {

    //生成 Cookie 键值对，返回前端浏览器存储； 键值对中存储的 是在每次请求后都会响应变化的 token 。
    private static final String COOKIE_NAME = "JWT_TOKEN";

    @RequestMapping("/user/login")
    public String login(){

        return "/login";
    }

    @RequestMapping("/user/logout")
    public String logout(){
        //注销 session

        return "redirect:/login";
    }

    public void captcha(){
        // 验证码处理

    }



    /**
     * index 测试
     * @return
     */
    @RequestMapping("/index")
    public String index(){
        // 测试前返回 index界面，只返回界面 不携带数据。
        return "/index";
    }

    @ResponseBody
    @RequestMapping("/index/test")
    public String test(){
        //测试只给前端返回数据
        return "测试，直接返回 字符串对象 的数据";
    }

}
