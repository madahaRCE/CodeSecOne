package com.madaha.codesecone.controller.unauth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 1. 信息泄露，敏感信息
 * 2. 普通用户登录后，管理员 用户-密码 泄露
 */
@RestController
@RequestMapping("/InfoLeak")
public class InfoLeak {

    /**
     * @poc http://127.0.0.1:28888/InfoLeak/vul
     * @return 用户名:密码
     */
    @RequestMapping("/vul")
    public String vul(){
        return "admin:password";
    }
}
