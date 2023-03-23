package com.madaha.codesecone.controller.AdvancedAttack.reflection;

import java.io.*;

public class Operation {
    public static void ser(Object obj){
        try{
            // 序列化操作，写数据
            // ObjectOutputStream 能把 Object 输出成 Byte 流。
            ObjectOutputStream oss = new ObjectOutputStream(new FileOutputStream("object.obj"));

            // 序列化关键函数
            oss.writeObject(obj);

            oss.flush();    // 缓冲流
            oss.close();    // 关闭流

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void deser(){
        try{
            // 反序列化操作，读取数据
            File file = new File("object.obj");
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));

            // 反序列化的关键函数
            // 反序列化，自动实例化，执行恶意恶意代码。
            Object x = ois.readObject();

            System.out.println(x);
            ois.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
