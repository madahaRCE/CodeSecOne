package com.madaha.codesecone.controller.BAC.BypassPermission;

import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;


/**
 * Tomcat: getRequestURI()方法，存在全校绕过漏洞。
 * 注意：只能用 tomcat+servlet 才存在权限绕过漏洞，如果用springboot框架进行路由处理的话 权限绕过漏洞就不存在了！！
 *
 * Filter权限验证：package com.madaha.codesecone.Filter.BypassPermissionFilter
 * Filter注册配置：package com.madaha.codesecone.config.FilterConfig
 *
 * @poc http://127.0.0.1:28888/bypassPermission/api/..;/admin
 */

// @RestController
@RequestMapping("/bypassPermission")
public class BypassPermission_SpringBoot_No_Exist {

    /**
     * 黑名单访问，测试权限绕过问题。
     */
    @RequestMapping(value = "/admin")
    public String admin(){
        return "Admin Page，白名单权限绕过了！";
    }

    /**
     * 下均为白名单，测试url
     */
    @RequestMapping(value = "/api")
    public String api(){
        return "Api Page";
    }

    @RequestMapping(value = "/login")
    public String login(){
        return "Login Page";
    }


    // 测试url打印
    @RequestMapping("/index")
    public String index(HttpServletRequest request){
        String requestURL = request.getRequestURL().toString();
        String requestURI = request.getRequestURI();
        //String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();

        return "getRequestURL: " + requestURL + " ~~\n~~ " +
                "getRequestURI: " + requestURI + " ~~\n~~ " +
                "getServletPath: " + servletPath;
    }
}

/**
 *  ===============================================================================================
 *  权限绕过问题：可以看出 getServletPath 会对获取的字符进行url解码：
 *  (注意：getRequestURI 也只能绕过部分过滤方式)
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