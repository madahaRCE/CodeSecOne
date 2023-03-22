package com.madaha.codesecone.controller.xpath;

import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;

/**
 * 1. “注入” 这种攻击方式被列为了 OWASP 十大攻击的榜首。常见的注入方式是的 SQL 注入攻击。
 * 2. 而是相对较为冷门的 XPath 和 XQuery 注入攻击。
 * 3. Query 是 XPath 语言的超集，增加了一些类似于 SQL 的语法和非常实用的函数来让我们更方便的查询 XML 文档。
 */
@RestController
@RequestMapping("/XPath")
public class XPathInjection {

    Logger log = LoggerFactory.getLogger(XPathInjection.class);

    /**
     * XPath 注入是一种特殊的注入攻击，它利用应用程序使用 XPath 查询 XML 文档的特殊来实现攻击
     * 输入 admin' or '1' = '1
     * @XPath语句：  /users/user[username='admin' or '1'='1' and password='aa']
     *
     * @poc http://127.0.0.1:28888/XPath/vul?username=admin%27%20or%20%271%27%20=%20%271&password=xxxxx
     * @decode http://127.0.0.1:28888/XPath/vul?username=admin' or '1' = '1&password=xxxxx
     */
    @GetMapping("/vul")
    public String vul(@RequestParam("username") String username, @RequestParam("password") String password){
        try {
            // 构造 XML 文档
            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(new InputSource(new StringReader("<users>"
                            + "<user>"
                            + "<username>admin</username>"
                            + "<password>abc123123</password>"
                            + "</user>"
                            + "</users>")));

            // 解析 XML 文档
            // XPathConstants.NODESET (xpath常量.节点集)
            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList nodes = (NodeList) xpath.evaluate("/users/user[username='" + username + "' and password='" + password + "']", doc, XPathConstants.NODESET);

            // 检查查询结果
            if(nodes.getLength() > 0){
                // 用户名和密码验证通过：
                log.info("[vul] xpath注入成功");
                return "用户名和密码验证通过！";
            } else {
                // 用户名和密码验证失败：
                log.info("[vul] xpath注入失败！");
                return "用户名密码错误！";
            }
        }catch (Exception e){
            log.error("[vul] 发生异常： " + e.getMessage(), e);
            return "发生异常： " + e.getMessage();
        }
    }

    /**
     * XPath 转义
     * 输入：admin' or '1' = '1
     * 转义：admin&apos; or &apos;1&apos; = &apos;1
     */
    @GetMapping("/safe")
    public String safe(@RequestParam("username") String username, @RequestParam("password") String password){
        // StringEscapeUtils.escapeXml10() 是 Apache Commons Lang 库中的一个方法，该方法会将字符串中的 &、<、>、' 和 " 转换为实体引用，以便安全地在 XML 文档中使用这些字符。
        String escapeUsername = StringEscapeUtils.escapeXml10(username);
        String escapePassword = StringEscapeUtils.escapeXml10(password);
        log.info("[vul] xpath 转义：" + escapeUsername + escapePassword);

        try {
            // 构造 XML 文档
            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(new InputSource(new StringReader("<users>"
                            + "<user>"
                            + "<username>admin</username>"
                            + "<password>abc123123</password>"
                            + "</user>"
                            + "</users>")));

            // 解析 XML 文档
            // XPathConstants.NODESET (xpath常量.节点集)
            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList nodes = (NodeList) xpath.evaluate("/users/user[username='" + escapeUsername + "' and password='" + escapePassword + "']", doc, XPathConstants.NODESET);

            // 检查查询结果
            if(nodes.getLength() > 0){
                // 用户名和密码验证通过：
                log.info("[safe] xpath注入成功");
                return "用户名和密码验证通过！";
            } else {
                // 用户名和密码验证失败：
                log.info("[safe] xpath注入失败！");
                return "用户名密码错误！";
            }
        }catch (Exception e){
            log.error("[safe] 发生异常： " + e.getMessage(), e);
            return "发生异常： " + e.getMessage();
        }
    }
}
