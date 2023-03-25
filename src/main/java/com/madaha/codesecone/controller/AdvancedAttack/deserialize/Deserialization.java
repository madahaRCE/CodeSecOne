package com.madaha.codesecone.controller.AdvancedAttack.deserialize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 反序列化漏洞：序列化是将 Java 对象转换成字节流的过程，而反序列化是将字节流转换成 Java 对象的过程。
 * <p>
 *     如何发现漏洞：
 *        1. Sink，使用了序列化和反序列化操作的方法；
 *        2. Source，将用户输入的序列化数据直接反序列化为对象；
 *        3. Sanitizer，没有对序列化数据进行任何校验或过滤；
 *
 *     Java 常见的序列化和反序列化的方法有 Java 远程序列化 和 JSON 类（fastjson、jackson）序列化。
 * </p>
 * <p>
 *     序列化方法：
 *        1. ObjectInputStream.readObject（JDK）      // ObjectInputStream.readUnshared（safe：不会执行任意代码，而是只将序列化数据恢复为原始对象。）
 *        2. XMLDecode.readObject
 *        3. Yaml.load
 *        4. XStream.fromXML
 *        5. ObjectMapper.readValue
 *        6. JSON.paresObject
 *        ....
 * </p>
 */

@RestController
@RequestMapping("/Deserialize/readObject")
public class Deserialization {
    Logger log = LoggerFactory.getLogger(Deserialization.class);








}
