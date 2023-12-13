package com.madaha.codesecone.Filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BypassPermissionFilter implements Filter {

    /**
     * Tomcat: getRequestURI()方法，存在全校绕过漏洞。
     *
     * @poc http://10.201.170.88:28888/bypassPermission/api/..;/admin
     *
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        // 白名单url，自定义可以放行的请求。
        String[] whiteUrl = new String[]{"/bypassPermission/api", "/bypassPermission/login", "/bypassPermission/index"};

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI();   // getRequestURI() 方法没有对请求进行过滤，是Tomcat权限绕过的漏洞所在。

        boolean doFilter = true;
        for (int i = 0; i < whiteUrl.length; i++) {
            if (uri.startsWith(whiteUrl[i])) {
                doFilter = false;
                break;  // 不在继续往下进行，终止HTTP请求。
            }
        }

        if (doFilter) {
            System.out.println("测试BypassPermissionFilter，匹配结果no满足白名单：  " + "response.sendRedirect(\"/bypassPermission/login\");");
            response.sendRedirect("/bypassPermission/login");
        } else {
            System.out.println("测试BypassPermissionFilter，匹配结果满足白名单：   " + "filterChain.doFilter(servletRequest, servletResponse);");
            filterChain.doFilter(servletRequest, servletResponse);
        }
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