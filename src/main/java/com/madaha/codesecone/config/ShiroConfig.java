package com.madaha.codesecone.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

@Configuration
public class ShiroConfig {

    // 路径过滤规则
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 如果不设置默认会自动寻找web工程根目录下的“/login.jsp”页面
         shiroFilterFactoryBean.setLoginUrl("/Shiro/serialize/shiroLogin");
        // shiroFilterFactoryBean.setSuccessUrl("/Shiro/serialize/index"); //因为我不需要登录成功，这里只是为了测试remember反序化漏洞，所以可以注释掉！！！

        //拦截器
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        // 配置不会被拦截的链接 （按顺序判断）
        // 对静态资源设置匿名访问，这里不使用该框架，因此 注释掉就行了。
        // map.put("/static/**", "anon");
        // map.put("/css/**", "anon");
        // map.put("/js/**", "anon");
        map.put("/Shiro/key/**", "anon");
//        map.put("/Shiro/serialize/shiroLogin", "anon");

        // 过滤链定义，从上向下顺序执行，一般将/**放在最为下边， 进行身份认证后才能访问
        // authc：所有url都必须认证通过才可以访问；
        // anon：所有url都可以匿名访问
        // user：指的是用户认证通过或者配置了Remeber Me记住用户登录状态后可访问
        map.put("/Shiro/serialize/**", "user");
//        map.put("/Shiro/**", "user");


        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }


    // cookie对象
    public SimpleCookie rememberMeCookie() {
        // 设置cookie名称，对应login.html的<input type="checkbox" name="rememberMe"/>
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        // 设置cookie的过期时间，单位为妙，这里为一天
        cookie.setMaxAge(86400);
        return cookie;
    }

    // cookie管理对象
    // rememberMeManager()方法是生成rememberMe管理器，而且要将这个rememberMe管理器设置到securityManager中
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());

        // rememberMe cookie加密的秘钥 建议每个项目都不一样 默认AES算法 秘钥长度（128 256 512 位）
        // 如果不进行秘钥设置，就使用默认的秘钥。
        // cookieRememberMeManager.setCipherKey(Base64.decode("3AvVhmFLUs0KTA3Kprsdag=="));

        return cookieRememberMeManager;
    }

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();

        // 设置realm，这里必须要设置realm  （注意：必须要配置！！！！）
        defaultWebSecurityManager.setRealm(myShiroRealm());

        // 添加 rememberMeManager
        // 用户 授权/认证 信息的Cache
        defaultWebSecurityManager.setRememberMeManager(rememberMeManager());

        return defaultWebSecurityManager;
    }


    /**
     * 凭证匹配器：
     *     （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了）
     *
     *   因为我们的密码是加过密的，所以，如果要Shiro验证用户身份的话，需要告诉它我们用的是md5加密的，并且是加密了两次。
     *   同时我们在自己的Realm中也通过SimpleAuthenticationInfo返回了加密时使用的盐。
     *   这样Shiro就能顺利的解密密码并验证用户名和密码是否正确了。
     */
//    public HashedCredentialsMatcher hashedCredentialsMatcher(){
//        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
//        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
//        hashedCredentialsMatcher.setHashIterations(2);//散列的次数，比如散列两次，相当于 md5(md5(""));
//        return hashedCredentialsMatcher;
//    }

    @Bean
    public MyShiroRealm myShiroRealm(){
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        // myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());// 设置解密规则

        return myShiroRealm;
    }

}
