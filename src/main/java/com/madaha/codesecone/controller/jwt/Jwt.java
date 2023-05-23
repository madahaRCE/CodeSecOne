package com.madaha.codesecone.controller.jwt;

import com.madaha.codesecone.util.CookieUtils;
import com.madaha.codesecone.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.security.timestamp.TSRequest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/jwt")
public class Jwt {

    private static final String COOKIE_NAME = "USER_COOKIE";

    /**
     * Create jwt token and set token to cookies.
     * http://127.0.0.1:28888/jwt/createToken
     *
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/createToken")
    public String createToken(HttpServletRequest request, HttpServletResponse response){
        // String loginUser = request.getUserPrincipal().getName();
        String loginUser = request.getSession().getAttribute("LoginUser").toString();
        log.info("Current login user is " + loginUser);

        CookieUtils.deleteCookie(response, COOKIE_NAME);
        String token  = JwtUtils.generateTokenByJavaJwt(loginUser);
        Cookie cookie = new Cookie(COOKIE_NAME, token);

        cookie.setMaxAge(86400);    // 1 DAY
        cookie.setPath("/");
        cookie.setSecure(true);

        response.addCookie(cookie);
        return "Add jwt token cookie successfully, Cookie name is USER_COOKIE";
    }

    /**
     * Get nickname from USER_COOKIE
     * http://127.0.0.1:28888/jwt/getName
     *
     * @param user_cookie cookie
     * @return nickname
     */
    @RequestMapping("/getName")
    public String getNickname(@CookieValue(COOKIE_NAME) String user_cookie){
        String nickname = JwtUtils.getNicknameByJavaJwt(user_cookie);
        return "Current jwt user is " + nickname;
    }
}
