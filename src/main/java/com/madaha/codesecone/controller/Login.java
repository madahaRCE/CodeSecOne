package com.madaha.codesecone.controller;

import com.madaha.codesecone.service.LoginService;
import com.madaha.codesecone.service.serviceimpl.LoginServiceImpl;
import com.madaha.codesecone.util.JwtUtils;
import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//这里别用@RestController,用了你的请求会给你返回个json。这里要的是路由
@Controller
public class Login {

    /**
     * 穿件JWT_Token：
     *    （1）生成Token，将其存出来HTTP响应头的 Set-Cookie 键值对中，返回前端浏览器存储；
     *    （2）在每次HTTP请求时，request域中都会带上 Cookies 键值对，存储的 是在每次请求后都会响应变化的 token 。
     */
    private static final String COOKIE_NAME = "JWT_TOKEN";

    @Autowired
    LoginService loginService = new LoginServiceImpl();

    @RequestMapping("/user/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password,
                        @RequestParam("captcha") String captcha, Model model, HttpSession session,
                        HttpServletRequest request, HttpServletResponse response){


        /**
         * 图片验证码验证：将前端form表单请求的“captcha”参数，与服务器上 验证码生成器存储的“captcha”验证码 进行验证。
         * 注意：服务端生成的验证码存储再session（会话）中，具体是怎存储的我没有弄明白，有哪位大佬懂的原理的，评论下 教教我。
         */
        if(!CaptchaUtil.ver(captcha, request)){
            CaptchaUtil.clear(request);
            model.addAttribute("msg", "验证码不正确！");
            return "/login";
        }



        /**
         * 此处，仅是为了登录：
         *      （1）创建登录session；
         *      （2）创建cookie，或者是创建token存储再cookie中（例如：创建一个 jwt 形式的 token）；
         *      （3）注：此处的cookie，是为了其他功能进行客户端user验证用的。
         *
         * token是存储在客户端cookie中的凭证：
         *      （1）每一次请求都需要携带 token，需要把 token 放到 HTTP 的 Header 里；
         *      （2）基于 token 的用户认证是一种 “服务端无状态” 的认证方式，服务端不用存放 token 数据。用解析 token 的计算时间换取 session 的存储空间，从而减轻服务器的压力，减少频繁的查询数据库；（时间换空间！！！）
         *      （3）token 完全由应用管理，所以它可以避开同源策略。
         *
         * 登录逻辑：
         *      1、用户名、密码 登录验证
         *      2、创建Cookie（token）对象，加入到response对象中，返回到前端浏览器；
         *      3、给session对象设置“key-value”，返回前端浏览器；唯一的 ”Session ID“ 记录唯一浏览器用户。
         *
         * 注意1：此处验证码逻辑漏洞绕过，后端登录服务，未对每次登录 进行验证码更新；
         */
        if(loginService.loginService(username, password)) {

            /**
             * Cookie:JWT_Token;
             *    (1) 创建JWT Token，并将其添加到Cookie对象中（HTTP请求头中）。
             *    (2) HTTP Cookie（也叫 Web Cookie 或浏览器 Cookie）是服务器发送到用户浏览器并保存在本地的一小块数据。
             *    (3) 服务端返回的Set-Cookie会存储在浏览器中，流量器将 cookie 存储，并在下次向 同一服务器 再发起请求时携带并发送到服务器上。
             *    (4) Cookie用于告知服务端两个请求是否来自同一浏览器——如保持用户的登录状态；Cookie 使基于无状态的 HTTP 协议记录稳定的状态信息成为了可能。
             */
            String token = JwtUtils.generateTokenByJjwt(username);
            Cookie cookie = new Cookie(COOKIE_NAME, token);

            // 在为使用Jwt_Token时，可以使用如下cookie键值对创建方式，
            // Cookie cookie = new Cookie( "username", username);

            cookie.setHttpOnly(true);
            cookie.setMaxAge(60 * 60 * 24);     // 此处设置1天时间过期。   如下范例：设置7天过期 (7 * 24 * 60 * 60)
            cookie.setPath("/");


            /**
             * 设置cookie对象返回浏览器：
             *     （1）注意！此处Cookie是否添加信息 都不会影响用户登录验证，因为登录拦截器验证的为是否有session会话信息。
             *     （2）增加cookie的jwt_token，或者是直接添加username=xxx字段，是为了后端进行权限验证。
             *     （3）业务操作过程中：第一步是为了user进行登录认证，第二步是基于user角色进行权限校验。（解释：认证和授权的区别！！）
             */
            response.addCookie(cookie);


            /**
             * 创建session过程与使用流程：
             *      （1）第一次访问时，服务器会创建一个新的session，并且把session的Id以cookie的形式发送给客户端浏览器。（session存储在服务端。）
             *      （2）第二次访问时候，浏览器交出cookie，服务器找到对应的Session。
             *      （3）注：当浏览器禁用了cookie后，用URL重写（后面带上一个类似cookie的东西）这种解决方案解决Session数据共享问题。
             *
             * 声明：为什么创建session会话“key-value”对？
             *      （1）创建session会话“key-value”对，只要浏览器不关闭，session对象就不会被销毁（注意：是服务器上存活的有效的session对象）；
             *      （2）另一方面，是为了在登录拦截器处理类 “HandlerInterceptor” 中，对session的 “LoginUser” 进行验证；
             *      （3）当拦截器判断session对象不为null时，放行访问（通过session会话，确认客户端是否登录）。
             */
            session.setAttribute("LoginUser", username);


            // 以上验证通过后，返回默认页面。（ [1]验证码验证通过;  [2]cookie/token验证通过。 ）
            return "redirect:/index";

        }else {
            model.addAttribute("msg", "用户名或密码错误");
            return "/login";
        }
    }

    /**
     *  客户端请求注销，销毁客户端当前使用的 session 对象；
     *
     * @param session
     * @return
     */
    @RequestMapping("/user/logout")
    public String logout(HttpSession session, HttpServletResponse response){
        //注销登录，作废 session 对象
        session.invalidate();

        // 如果用户退出登录了，也应该重置对应的Cookie信息。
        Cookie cookie = new Cookie(COOKIE_NAME, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/login";
    }

    /**
     * 图片验证码，图片验证码的截图非常简单。
     * 只需要 导入pom依赖，前端html页面请求添加，即可实现验证码 功能。
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 验证码处理：生成验证码，并返回前段图片
        CaptchaUtil.out(request,response);
    }

    /**
     * index 测试
     * @return
     */
    @RequestMapping("/index")
    public String index(){
        // 测试前返回 index界面，只返回界面 不携带数据。
        return "/index";
    }

    @ResponseBody
    @RequestMapping("/index/test")
    public String test(){
        //测试只给前端返回数据
        return "测试，直接返回 字符串对象 的数据";
    }

    @RequestMapping("/amis-rce-page")
    public String amis_hello(){
        return "amis-rce-page";
    }

}
