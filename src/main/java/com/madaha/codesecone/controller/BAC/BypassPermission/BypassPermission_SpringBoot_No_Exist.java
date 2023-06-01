package com.madaha.codesecone.controller.BAC.BypassPermission;


import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;



// 只能用tomcat+servlet才存在权限绕过漏洞，如果用springboot的话 权限绕过漏洞就不存在了！！
// @RestController
@RequestMapping("/bypassPermission")
public class BypassPermission_SpringBoot_No_Exist {

    /**
     * 黑名单访问，测试权限绕过问题。
     */
    @RequestMapping(value = "admin")
    public String admin(){
        return "Admin Page";
    }

    /**
     * 下均为白名单，测试url
     */
    @RequestMapping(value = "api")
    public String api(){
        return "Api Page";
    }

    @RequestMapping(value = "login")
    public String login(){
        return "Login Page";
    }

    @RequestMapping("/index")
    public String index(HttpServletRequest request){
        String requestURL = request.getRequestURL().toString();
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();

        return "getRequestURL: " + requestURL + " ~~\n~~ " +
                "getRequestURI: " + requestURI + " ~~\n~~ " +
                "getServletPath: " + servletPath;
    }
}



/**
 *  ===============================================================================================
 *  权限绕过问题：可以看出 getServletPath 会对获取的字符进行url解码：
 *  ===============================================================================================
 *      payload	          getRequestURL	                         getRequestURI	    getServletPath
 *      /index	          http://127.0.0.1:8081/index	         /index	            /index
 *      /./index	      http://127.0.0.1:8081/./index	         /./index	        /index
 *      /.;/index	      http://127.0.0.1:8081/.;/index	     /.;/index	        /index
 *      /a/../index	      http://127.0.0.1:8081/a/../index	     /a/../index	    /index
 *      /a/..;/index      http://127.0.0.1:8081/a/..;/index	     /a/..;/index	    /index
 *      /;/index	      http://127.0.0.1:8081/;/index	         /;/index	        /index
 *      /;a/index	      http://127.0.0.1:8081/;a/index         /;a/index	        /index
 *      /%2e/index	      http://127.0.0.1:8081/./index          /%2e/index	        /index
 *      /inde%78	      http://127.0.0.1:8081/index            /inde%78	        /index
 *  ===============================================================================================
 */