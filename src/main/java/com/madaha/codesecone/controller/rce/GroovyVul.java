package com.madaha.codesecone.controller.rce;


import groovy.lang.GroovyShell;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/RCE/Groovy")
public class GroovyVul {

    /**
     * @poc http://127.0.0.1:28888/RCE/Groovy/vul?cmd="calc".execute()
     * @param cmd
     */
    @GetMapping("/vul")
    public void vul(String cmd){
        GroovyShell shell = new GroovyShell();
        shell.evaluate(cmd);
    }

    /**
     * 定义一个黑名单命令执行，存在被绕过的问题。
     * 建议：可以定义一个命令执行的白名单。
     * @param cmd
     */
    @GetMapping("/safe")
    public void safe(String cmd){
        // 定义一个列表来存储安全的代码，不利于维护;
        // http://127.0.0.1:28888/RCE/Groovy/safe?cmd=%22notepad%22.execute()
        List<String> safeBlackList = Arrays.asList("\"id\".execute()", "\"whoami\".execute()", "\"notepad\".execute()");
        if(!safeBlackList.contains(cmd)){
            throw new RuntimeException("unsafe code detecate: " + cmd);
        }

//        List<String> safeWhiteList = Arrays.asList("\"calc\".execute()");
//        if (safeWhiteList.contains(cmd)){
//            throw new RuntimeException("unsafe code detecate: " + cmd);
//        }

        GroovyShell shell = new GroovyShell();
        shell.evaluate(cmd);

    }
}
