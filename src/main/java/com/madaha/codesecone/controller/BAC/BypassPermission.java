package com.madaha.codesecone.controller.BAC;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/bypassPermission")
public class BypassPermission {
















    /**
     * http://127.0.0.1:28888/bypassPermission/index
     *
     * @param request
     * @return routeString
     */
    @ResponseBody
    @RequestMapping("/index")
    public String index(HttpServletRequest request){
        String requestURL = request.getRequestURL().toString();
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();

        return "getRequestURL: " + requestURL + "\n" +
                "getRequestURI: " + requestURI + "\n" +
//                "getContextPath: " + contextPath + "\n" +
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