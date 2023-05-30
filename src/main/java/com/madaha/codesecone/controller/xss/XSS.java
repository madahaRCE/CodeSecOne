package com.madaha.codesecone.controller.xss;

import com.madaha.codesecone.util.SecurityUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
 import org.owasp.esapi.ESAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * XSS：反射形/存储型
 * 修复：xss过滤，xss转码
 */
@RestController
@RequestMapping("/XSS")
public class XSS {
    static Logger log = LoggerFactory.getLogger(XSS.class);

    /**
     * reflect
     *    @poc http://127.0.0.1:28888/XSS/reflect?xss=%3Cscript%3Ealert(111)%3C/script%3E
     *    @decode http://127.0.0.1:28888/XSS/reflect?xss=<script>alert(111)</script>
     * @param xss
     * @return
     */
    @GetMapping("/reflect")
    public static String reflect(String xss){
        log.info("[vul] 反射型XSS: " + xss);
        return xss;
    }

    /**
     * @poc http://127.0.0.1:28888/XSS/reflect2?xss=%3Cscript%3Ealert(111)%3C/script%3E
     * @decode http://127.0.0.1:28888/XSS/reflect2?xss=<script>alert(111)</script>
     *
     * @param xss
     * @param response
     */
    @GetMapping("reflect2")
    public static void reflect2(String xss, HttpServletResponse response){
        // 修复，设置ContentType类型：response.setContentType("text/plain;charset=utf-8");
        try{
            // 设置如下响应头，就不会再出现 反射型 xss 问题！！！
            // response.setContentType("text/plain;charset=utf-8");
            response.getWriter().println(xss);
            response.getWriter().flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    /**
     * Vul Code.
     * StoredXSS step1
     * @poc_step1 http://127.0.0.1:28888/XSS/stored/store?xss=%3Cscript%3Ealert(111)%3C/script%3E
     * @decode http://127.0.0.1:28888/XSS/stored/store?xss=<script>alert(111)</script>
     *
     * @param xss unescape string
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("/stored/store")
    public String store(String xss, HttpServletResponse response){
        Cookie cookie = new Cookie("xss", xss);
        response.addCookie(cookie);
        return "Set param into cookie";
    }

    /**
     * Vul Code.
     * StoredXSS step2
     * @poc_step2(触发漏洞) http://127.0.0.1:28888/XSS/stored/show
     *
     * @param xss unescape string
     */
    @GetMapping("/stored/show")
    public String show(@CookieValue("xss") String xss) {
        return xss;
    }



    /**
     * safe Code
     * http://127.0.0.1:28888/XSS/safe?xss=<script>alert(111)</script>
     *
     * @param xss
     * @return
     */
    @GetMapping("/safe")
    public static String safe(String xss){
        return encode(xss);
    }
    public static String encode(String origin){
        origin = StringUtils.replace(origin, "&", "&amp;");
        origin = StringUtils.replace(origin, "<", "&lt;");
        origin = StringUtils.replace(origin, ">", "&gt;");
        origin = StringUtils.replace(origin, "\"", "&quot;");
        origin = StringUtils.replace(origin, "'", "&#x27;");
        origin = StringUtils.replace(origin, "/", "&#x2F;");
        return origin;
    }

    // 与上面 safe() 的过滤方法类似。
    @GetMapping("/filter")
    public static String filter(String xss){
        log.info("[safe] xss过滤： " + xss);
        return SecurityUtils.filterXss(xss);
    }


    /**
     * 采用实体编码：
     *    采用自带函数HtmlUtils.htmlEscape()来过滤。
     *
     * @param xss
     * @return
     */
    @GetMapping("/escape")
    public static String escape(String xss){
        log.info("[safe] htmlEscape实体编码： " + xss);
        return HtmlUtils.htmlEscape(xss);
    }


    /**
     * 富文本过滤：
     *    采用Jsoup做富文本过来
     *
     * org.jsoup.safety.Whitelist   是个白名单, 定义了什么html元素或者属性可以通过, 而其他的所有内容都将被删除。
     * org.jsoup.safety.Cleaner     是清理器，在创建Cleaner对象时告诉他白名单是什么，然后就可以用于请理危险元素和脚本了。
     *
     * @param xss
     * @return
     */
    @GetMapping("/whiteList")
    public static String whiteList(String xss){
        Whitelist whitelist = (new Whitelist())
                .addTags("p", "hr", "div", "img", "span", "textarea")   // 设置允许的标签
                .addAttributes("a", "href", "title")     // 设置标签允许的属性, 避免如mouseover属性
                .addProtocols("img", "src", "http", "https")  // img的src属性只允许http和https开头
                .addProtocols("a", "href", "http", "https");

        log.info("[safe] 富文本过滤：" + xss);
        return Jsoup.clean(xss, whitelist);
    }


    /**
     * ESAPI： 采用 ESAPI 过滤。
     */

    // 因为每次请求都报错，所以猜测可能是没有使用 ESAPI.properties 配置文件，这几直接找了一份配置文件，直接拷贝过来的。
    // 没什么作用，先删除 测试其他功能。

    /**
     * 解决办法找到了：
     *    解决方法：将esapi.jar添加到项目之后，还要引入ESAPI.properties和validation.properties两个文件。
     *    参考链接：https://blog.csdn.net/woqq773743943/article/details/65630452
     * @param xss
     * @return
     */
    @GetMapping("/ESAPI")
    @ResponseBody
    public static String esapi(String xss){
        log.info("[safe] ESAPI: " + xss);

        // 注：有问题，返回不了数据到前端页面。
        return ESAPI.encoder().encodeForHTML(xss);
    }
}
