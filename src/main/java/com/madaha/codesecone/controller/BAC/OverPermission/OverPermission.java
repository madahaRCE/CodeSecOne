package com.madaha.codesecone.controller.BAC.OverPermission;

/**
 * 越权访问（Broken Access Control，简称BAC）:
 *    1、水平越权
 *    2、垂直越权
 *
 * 参考范例：
 *    org.joychou.controller.Cookies;  (java-sec-code)
 *    com.best.hello.controller.BAC;   (Hello-java-Sec)
 *
 * 权限验证：
 *    （1）在LoginHandlerInterceptor中 对session进行判断，拦截所有未登录的用户 并从定向到login.html界面。
 *       Object session = request.getSession().getAttribute("LoginUser");
 *    （2）在Login的@Controller中 通过验证用户名和密码是否正确，验证通过就生成JWT_Token，并将JWT_Token返回给客户端；
 *       String token = JwtUtils.generateToken(username);
 *       Cookie cookie = new Cookie(COOKIE_NAME, token);
 *    （3）验证用户授权，通过 JWT_Token 获取用户信息（username），进行判断当前request的用户是谁？时候有权限进行相关操作？
 *       权限验证，就是获取username所具有的角色，判断当前用户是否拥有对应的操作权限。
 */
public class OverPermission {

}
