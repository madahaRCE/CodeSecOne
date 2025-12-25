package com.madaha.codesecone.controller.AdvancedAttack.deserialize;

import com.madaha.codesecone.util.WebUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/XStreamRCE")
public class XStream_Vul {

    // poc post  http://127.0.0.1:28888/XStreamRCE/vul
    @RequestMapping("/vul")
    public String parseXML(HttpServletRequest request, HttpServletResponse response) {
        try {
            String xml = WebUtils.getRequestBody(request);
            XStream xStream = new XStream(new DomDriver());
            xStream.fromXML(xml);
        } catch (Exception e){
            return e.toString();
        }
        return "xstream test";
    }

    // POCl链接：  https://www.cnblogs.com/NoCirc1e/p/16275607.html

    // POC写在文件里了，因为是使用哦的POST发包触发：
    // C:\Users\ThreatBook\IdeaProjects\CodeSecOne\src\main\java\com\madaha\codesecone\controller\AdvancedAttack\deserialize\Exploit\XStreamEXP.txt
}