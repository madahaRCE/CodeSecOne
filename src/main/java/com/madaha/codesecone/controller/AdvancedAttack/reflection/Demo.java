package com.madaha.codesecone.controller.AdvancedAttack.reflection;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * 序列化与反序列化测试，基于放射机制，实现命令执行；
 */
public class Demo implements Serializable {
    private Integer age;
    private String name;
    public Demo(){}
    // 构造函数，初始化时执行
    public Demo(String name, Integer age){
        this.age = age;
        this.name = name;
    }

    // 漏洞利用点，实现自定义反序列化方法，并存在危险命令执行问题；
    private void readObject(java.io.ObjectInputStream inputStream) throws IOException, ClassNotFoundException{
        // 调用原始的 readObject 方法
        inputStream.defaultReadObject();

        // 通过反射方法执行命令；
        try{
            Method method = java.lang.Runtime.class.getMethod("exec", String.class);
            Object result = method.invoke(Runtime.getRuntime(), "calc");

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
