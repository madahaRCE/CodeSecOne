package com.madaha.codesecone.controller.sopCors;

import com.madaha.codesecone.config.SafeDomain_JsonpWebConfig;
import com.madaha.codesecone.util.JwtUtils;
import com.madaha.codesecone.util.SecurityUtils;
import com.madaha.codesecone.util.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * 前端 Jsonp 跨域：
 *
 * 1、跨域 SOP，全称为同源策略 (Same Origin Policy)：
 *    （1）浏览器受到了同源策略的限制，不允许实现跨域访问。
 *    （2）但是由于在开发过程中，其中的前后端的交互过程中不可避免会涉及到跨域的请求（设计同源策略的人想必也发现了这个问题），于是设计者给我们留了一个后门，就是只要服务器响应头中返回允许这个源的选项，那么跨域请求就会成功。
 *    （3）注意：不要认为浏览器默认支持同源策略就意味着不同源的请求就不能发出去，其实还是能发出去的，只是要看响应头中的设置 判断浏览器是否拦截响应数据。
 *
 * 2、跨域问题解决办法：
 *    （1）同源策略免疫的，有 <img> 的src 、<link> 的 href 还有就是<script>的 src , 那么JSONP 就是利用其中的 <script> 标签的sec 属性实现跨区域请求的。
 *    （2）服务器的响应头是否设置 Access-Control-Allow-Origin 值， 确认是否允许 http://x.x.x.x 的访问，不符合要求 那么浏览器便会将其给拦截。
 */
/**
 * JSONP劫持漏洞，利用
 *    (1)通过JSONP技术可以实现数据的跨域访问，必然会产生安全问题.
 *    (2)如果网站B对网站A的 JSONP 请求没有进行安全检查直接返回数据，则网站B 便存在JSONP 漏洞，网站A 利用 JSONP漏洞 能够获取用户在网站B上的数据。
 */

@Slf4j
@RestController
@RequestMapping("/jsonp")
public class Jsonp {

    private static final String COOKIE_NAME = "JWT_TOKEN";

    private String callback = SafeDomain_JsonpWebConfig.getBusinessCallback();


    /**
     * Set the response content-type to application/javascript.
     * http://127.0.0.1:28888/jsonp/vuln/referer?callback_=test_jsonp
     */
    @RequestMapping(value = "/vuln/referer", produces = "application/javascript")
    public String referer(HttpServletRequest request, @CookieValue(COOKIE_NAME) String user_cookie){
        String callback = request.getParameter(this.callback);
        return WebUtils.json2Jsoup(callback, JwtUtils.getUserIdFromJjwtToken(user_cookie));
        // return WebUtils.json2Jsoup(callback, "admin_123");
    }


    /**
     * 如果 referer 为 null，直接绕过检查，这样判断是有问题的。
     *
     * Direct access does not check Referer, non-direct access check regerer.
     * Developer like to do jsonp testing like this.
     * 直接访问不检查引用，非直接访问检查引用。开发人员喜欢像这样做jsonp测试。
     *
     * http://127.0.0.1:28888/jsonp/vuln/emptyRefer?callback_=test_jsonp
     */
    @RequestMapping(value = "/vuln/emptyRefer", produces = "application/javascript")
    public String emptyRefer(HttpServletRequest request, @CookieValue(COOKIE_NAME) String user_cookie) {
        String referer = request.getHeader("referer");

        // referer为null，逻辑上绕过了检查。
        if (null != referer && SecurityUtils.checkURL(referer) == null){
            return "error";
        }

        String callback = request.getParameter(this.callback);
        return WebUtils.json2Jsoup(callback, JwtUtils.getUserIdFromJjwtToken(user_cookie));
    }


    /**
     * Safe code.
     * http://127.0.0.1:28888/jsonp/sec/checkReferer?callback_=test_jsonp
     *
     * curl http://127.0.0.1:28888/jsonp/sec/checkReferer?callback_=test_jsonp -H "Referer: http://joychou.org" -I
     */
    @RequestMapping(value = "/sec/checkReferer", produces = "application/javascript")
    public String safecode(HttpServletRequest request, @CookieValue(COOKIE_NAME) String user_cookie){
        String referer = request.getHeader("referer");

        if (SecurityUtils.checkURL(referer) == null) {
            return "error";
        }

        String callback = request.getParameter(this.callback);
        return WebUtils.json2Jsoup(callback, JwtUtils.getUserIdFromJjwtToken(user_cookie));
    }






//////////////////////////////////////////////////////////////////////////////////////////
// 以下，是通过使用 CSRF_Token 进行校验，注：此处我并没有进行实现。
//////////////////////////////////////////////////////////////////////////////////////////
//
//    /**
//     * http://localhost:8080/jsonp/getToken?tokenCallback=aa
//     *
//     * object to jsonp
//     */
//    @GetMapping("/getToken")
//    public CsrfToken getCsrfToken1(CsrfToken token) {
//        return token;
//    }

//////////////////////////////////////////////////////////////////////////////////////////
// 以上，是通过使用 CSRF_Token 进行校验，注：此处我并没有进行实现。
//////////////////////////////////////////////////////////////////////////////////////////
}
