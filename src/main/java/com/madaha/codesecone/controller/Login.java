package com.madaha.codesecone.controller;

import com.madaha.codesecone.service.LoginService;
import com.madaha.codesecone.service.serviceimpl.LoginServiceImpl;
import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.UsesSunMisc;
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
     * 生成 Cookie 键值对，返回前端浏览器存储；
     * 键值对中存储的 是在每次请求后都会响应变化的 token 。
     * 但是，由于token太麻烦，耽误太多时间了，这里直接自定义一个字符串
     */
    //private static final String COOKIE_NAME = "JWT_TOKEN";

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
         * 1、用户名、密码 登录验证
         * 2、创建Cookie对象，加入到response对象中，返回前段浏览器；
         * 3、给session对象设置“key-value”，返回前端浏览器；
         *
         * 注意：此处验证码逻辑漏洞绕过，后端服务 未进行验证码更新；
         */
        if(loginService.loginService(username, password)) {
            /**
             * 创建JWT Token，将其添加到Cookie对象中。
             * 由于穿件JWT Token太麻烦，我这里耽误了太多时间，故此处跳过。
             */
            //String token = JwtUtils.generateToken(username);
            //Cookie cookie = new Cookie(COOKIE_NAME, token);

            /**
             * HTTP Cookie（也叫 Web Cookie 或浏览器 Cookie）是服务器发送到用户浏览器并保存在本地的一小块数据。
             * 浏览器会存储 cookie 并在下次向同一服务器再发起请求时携带并发送到服务器上。
             * 通常，它用于告知服务端两个请求是否来自同一浏览器——如保持用户的登录状态。
             * Cookie 使基于无状态的 HTTP 协议记录稳定的状态信息成为了可能。
             */
            Cookie cookie = new Cookie( "username", username);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(60 * 60 * 24);     // 此处设置1天时间过期。   如下范例：设置7天过期 (7 * 24 * 60 * 60)
            cookie.setPath("/");
            response.addCookie(cookie);         //设置cookie对象返回浏览器

            /**
             * 创建会话“key-value”对，只要浏览器不关闭，session对象就不会被销毁。
             * 另一方面，是为了在登录拦截器处理类 “HandlerInterceptor” 中，对session的 “LoginUser” 进行验证；
             * 当拦截去判断session对象不为null时，放行访问。
             */
            session.setAttribute("LoginUser", username);

            return "redirect:/index";
        }else {
            model.addAttribute("msg", "用户名或密码错误");
            return "/login";
        }
    }

    @RequestMapping("/user/logout")
    public String logout(HttpSession session){
        //注销登录，作废 session 对象
        session.invalidate();
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

}
