package com.madaha.codesecone.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//这里别用@RestController,用了你的请求会给你返回个json。这里要的是路由
@Controller
public class Login {

    @RequestMapping("/login")
    public String login(){

        return "/login";
    }

    @RequestMapping("/index")
    public String index(){

        return "/index";
    }

    @ResponseBody
    @RequestMapping("/index/test")
    public String test(){

        return "测试，直接返回 字符串对象 的数据";
    }

}
