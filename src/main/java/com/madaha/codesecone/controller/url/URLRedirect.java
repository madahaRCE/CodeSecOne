package com.madaha.codesecone.controller.url;

import com.madaha.codesecone.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * URL 重定向漏洞
 *
 * 审计的函数
 *   1. redirect
 *   2. forward
 *   3.setHeader
 */
@Controller
@RequestMapping("/Redirect")
public class URLRedirect {
    Logger log = LoggerFactory.getLogger(URLRedirect.class);

    /**
     * Spring：Redirect
     * @poc http://127.0.0.1:28888/Redirect/vul?url=http://www.baidu.com
     * @param url
     * @return
     */
    @GetMapping("/vul")
    public String vul(String url){
        log.info("[vul] 重定向访问： " + url);
        return "redirect:" + url;
    }

    /**
     * Servlet：Redirect
     *    @poc http://127.0.0.1:28888/Redirect/vul2?url=www.baidu.com
     * @param url
     * @return
     */
    @GetMapping("/vul2")
    public ModelAndView vul2(String url){
        return new ModelAndView("redirect://" + url);
    }

    /**
     * response.sendRedirect
     *    @poc http://127.0.0.1:28888/Redirect/vul3?url=http://www.baidu.com
     * @param url
     * @param response
     * @throws IOException
     */
    @GetMapping("/vul3")
    public void vul3(String url, HttpServletResponse response) throws IOException{
        response.sendRedirect(url);
    }

    /**
     * setHeader
     *    @poc http://127.0.0.1:28888/Redirect/vul4?url=http://www.baidu.com
     * @param request
     * @param response
     */
    @GetMapping("/vul4")
    @ResponseBody
    public static void vul4(HttpServletRequest request, HttpServletResponse response){
        String url = request.getParameter("url");
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);     // 301 redirect
        response.setHeader("Location", url);
    }

    /**
     * 白名单模式：
     *    @poc http://127.0.0.1:28888/Redirect/safe1?url=http://www.baidu.com
     * @param url
     * @return
     */
    @GetMapping("/safe1")
    @ResponseBody
    public String safe(String url){
        log.info("[safe] 安全重定向");
        if (SecurityUtils.isWhite(url)){
            return "安全域名:" + url;
        } else {
            return "非法重定向域名！！！";
        }
    }

    /**
     * 白名单模式：
     *    @poc http://127.0.0.1:28888/Redirect/safe2?url=http://www.baidu.com
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/safe2")
    public void safe2(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String url = request.getParameter("url");

        if (SecurityUtils.isWhite(url)){
           response.sendRedirect(url);
        }

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write("url forbidden");
        return;
    }

    /**
     * Safe code. Because it can only jump according to the path, it cannot jump according to other urls.
     * 安全代码。因为它只能根据路径跳转，所以不能根据其他网址跳转。
     *
     * @poc http://127.0.0.1:28888/Redirect/forwrad?url=/RCE/Runtime/val?cmd=calc
     */
    @RequestMapping("/forwrad")
    @ResponseBody
    public static void forward(HttpServletRequest request, HttpServletResponse response){
        String url = request.getParameter("url");
        RequestDispatcher rd = request.getRequestDispatcher(url);

        try{
            rd.forward(request, response);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
