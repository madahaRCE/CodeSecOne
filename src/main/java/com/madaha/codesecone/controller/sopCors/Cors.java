package com.madaha.codesecone.controller.sopCors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * cors 跨域访问
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
 * 跨域资源共享漏洞，利用需要满足两个条件
 *    1、Access-Control-Allow-Origin可控
 *    2、Access-Control-Allow-Credentials为true  ->  允许带上Cookie
 *
 *  验证方式：
 *    1.curl访问网站： curl https://www.junsec.com -H "Origin: https://test.com" -I
 *    2.检查返回包的 Access-Control-Allow-Origin 字段是否为https://test.com
 *    3.满足条件及返回数据，不满足条件浏览器拦截请求。注：服务端漏洞验证。
 */


@RestController
@RequestMapping("/CORS")
public class Cors {

    private static String info = "{\"name\":\"JoyChou\", \"phone\":\"18200001111\"}";

    /**
     * @验证方式 curl http://127.0.0.1:28888/CORS/vul -H "Origin: http://xxxx.com" -I
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/vuln/origin")
    public String vuls(HttpServletRequest request, HttpServletResponse response) {
        String origin = request.getHeader("origin");
        response.setHeader("Access-Control-Allow-Origin", origin);              // set origin from header
        response.setHeader("Access-Control-Allow-Credentials", "true");     // allow coolie
        return info;
    }


    /**
     * @验证方式 curl http://127.0.0.1:28888/CORS/vul -H "Origin: http://xxxx.com" -I
     * @param response
     * @return
     */
    @GetMapping("/vuln/setHeader")
    public String vuls2(HttpServletResponse response){
        // 后缀设置 Access-Control-Allow-Origin 为 * 的情况下，跨域的时候前端如果设置 withCredentials 为 true 会异常。
        response.setHeader("Access-Control-Allow-Origin", "*");
        return info;
    }


    /**
     * @验证方式  curl http://127.0.0.1:28888/CORS/vul -H "Origin: http://xxxx.com" -I
     * @return
     */
    @GetMapping("*")
    @RequestMapping("/vuln/crosOrigin")
    public String vuls3() { return info; }


    /**
     * @poc  http://127.0.0.1:28888/CORS/vul
     * @验证方式 curl http://127.0.0.1:28888/CORS/vul -H "Origin: http://xxxx.com" -I
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/vul")
    public String corsVul(HttpServletRequest request, HttpServletResponse response){
        // origin头，可控
        String origin = request.getHeader("origin");
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        return "cors vul";
    }



    /**
     * @验证方式 curl http://127.0.0.1:28888/CORS/vul -H "Origin: http://127.0.0.1" -I
     * @param request
     * @param response
     * @return
     */
    @CrossOrigin(origins = {"127.0.0.1", "http://127.0.0.1", "https://127.0.0.1"})
    @GetMapping("/safe")
    public String corsSafe(HttpServletRequest request, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return "cors safe";
    }


    /**
     * @验证方式 curl http://127.0.0.1:28888/CORS/vul -H "Origin: joychou.org" -I
     * @param request
     * @param response
     * @return
     */
    @CrossOrigin(origins = {"joychou.org", "http://test.joychou.me"})
    @GetMapping("/safe2")
    public String corsSafe2(HttpServletRequest request, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return "cors safe";
    }


    @CrossOrigin(origins = {"joychou.org", "http://test.joychou.me"})
    @GetMapping("/sec/crossOrigin")
    public String secCrossOrigin() {
        return info;
    }






//////////////////////////////////////////////////////////////////////////////////////////
// 以下，是通过使用 CSRF_Token 进行校验，注：此处我并没有进行实现。
//////////////////////////////////////////////////////////////////////////////////////////
//
//    /**
//     * WebMvcConfigurer设置Cors
//     * 支持自定义checkOrigin
//     * 代码：org/joychou/config/CorsConfig.java
//     */
//    @GetMapping("/sec/webMvcConfigurer")
//    public CsrfToken getCsrfToken_01(CsrfToken token) {
//        return token;
//    }
//
//
//    /**
//     * spring security设置cors
//     * 不支持自定义checkOrigin，因为spring security优先于setCorsProcessor执行
//     * 代码：org/joychou/security/WebSecurityConfig.java
//     */
//    @GetMapping("/sec/httpCors")
//    public CsrfToken getCsrfToken_02(CsrfToken token) {
//        return token;
//    }
//
//
//    /**
//     * 自定义filter设置cors
//     * 支持自定义checkOrigin
//     * 代码：org/joychou/filter/OriginFilter.java
//     */
//    @GetMapping("/sec/originFilter")
//    public CsrfToken getCsrfToken_03(CsrfToken token) {
//        return token;
//    }
//
//
//    /**
//     * CorsFilter设置cors。
//     * 不支持自定义checkOrigin，因为corsFilter优先于setCorsProcessor执行
//     * 代码：org/joychou/filter/BaseCorsFilter.java
//     */
//    @RequestMapping("/sec/corsFilter")
//    public CsrfToken getCsrfToken_04(CsrfToken token) {
//        return token;
//    }
//
//////////////////////////////////////////////////////////////////////////////////////////
// 以上，是通过使用 CSRF_Token 进行校验，注：此处我并没有进行实现。
//////////////////////////////////////////////////////////////////////////////////////////
}
