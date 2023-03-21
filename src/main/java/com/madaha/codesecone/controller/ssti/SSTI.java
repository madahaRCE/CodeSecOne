package com.madaha.codesecone.controller.ssti;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


/**
 * SSTI （服务端模板注入）
 * 漏洞本质：是 SPEL 表达式执行；
 *
 * @poc __$%7bnew%20java.util.Scanner(T(java.lang.Runtime).getRuntime().exec(%22calc.exe%22).getInputStream()).next()%7d__::.x
 *
 * 修复方法：
 *      （1）配置ResponseBody或RestController注解
 *      （2）通过 redirect: 跳转页面
 *      （3）方法参数中设置HttpServletResponse 参数
 *      （4）最为致命，直接升级更新Thymeleaf版本。
 */
@Controller
@RequestMapping("/SSTI")
public class SSTI {
    Logger log = LoggerFactory.getLogger(SSTI.class);

    /**
     * Spring Boot Thymeleaf 模板注入
     * 当请求 /path?lang=en 时，服务器去自动拼接待查找的模板文件名，为 /resources/templates/lang/en.html
     *
     * @decode http://127.0.0.1:28888/SSTI/thymeleaf/vul?lang=__${new java.util.Scanner(T(java.lang.Runtime).getRuntime().exec('calc').getInputStream()).next()}__::.x
     * @poc http://127.0.0.1:28888/SSTI/thymeleaf/vul?lang=__$%7bnew%20java.util.Scanner(T(java.lang.Runtime).getRuntime().exec(%27calc%27).getInputStream()).next()%7d__::.x
     */
    @GetMapping("/thymeleaf/vul")
    public String thymeleafVul(@RequestParam String lang){
        // 模板文件参数可控
        // template path is tainted(污染)
        return "lang/" + lang;
    }

    /**
     * @safe thymeleaf白名单
     * @poc http://127.0.0.1:28888/SSTI/thymeleaf/safe?lang=__$%7bnew%20java.util.Scanner(T(java.lang.Runtime).getRuntime().exec(%27calc%27).getInputStream()).next()%7d__::.x
     */
    @GetMapping("/thymeleaf/safe")
    public String thymeleafSafe(@RequestParam String lang){
        List<String> white_list = new ArrayList<>();
        white_list.add("en");
        white_list.add("ch");

        if (white_list.contains(lang)){
            return "lang/" + lang;
        }else {
            return "commons/401";
        }
    }


    /**
     * 根据spring Boot定义，如果controller无返回值，则以GetMapping的路由为视图名称，即将请求的url作为视图名称，调用模板引擎去解析；
     * 在这种情况下，我们只要可以控制请求的controller的参数，一样可以造成SSTI模板注入，最终实现RCE。
     *
     * @decode http://127.0.0.1:28888/SSTI/doc/vul/__${T(java.lang.Runtime).getRuntime().exec("calc")}__::.x
     * @poc http://127.0.0.1:28888/SSTI/doc/vul/__$%7BT(java.lang.Runtime).getRuntime().exec(%22calc%22)%7D__::.x
     */
    @GetMapping("/doc/vul/{document}")
    public void getDocumentVul(@PathVariable String document){
        log.info("[vul] SSTI payload: " + document);
    }

    /**
     * @safe 由于controller的参数被设置为HttpServletResponse，Spring人为它已经处理了 HTTP Response，因此不会发生视图名称解析。
     * @param document
     * @param response
     */
    @GetMapping("/doc/safe/{document}")
    public void getDocumentSafe(@PathVariable String document, HttpServletResponse response){
        log.info("[safe] SSTI payload: " + document);
    }
}
