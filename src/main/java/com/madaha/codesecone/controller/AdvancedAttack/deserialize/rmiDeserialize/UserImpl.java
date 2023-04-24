package com.madaha.codesecone.controller.AdvancedAttack.deserialize.rmiDeserialize;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

// 在 java.rmi.server.UnicastRemoteObject 的构造函数中将生 stub（存根） 和 skeleton（骨架）。
public class UserImpl extends UnicastRemoteObject implements User{
    // 必须有一个显示的构造函数（否则报错），并且要抛出一个RemoteException异常
    public UserImpl() throws RemoteException{
        super();
    }

    @Override
    public String name(String name) throws RemoteException {
        return name;
    }

    @Override
    public void say(String say) throws RemoteException {
        System.out.println("you speak" + say);
    }

    @Override
    public void dowork(Object work) throws RemoteException {
        System.out.println("you work is" + work);
    }

    @Override
    public Object getwork() throws RemoteException {
        // 1、返回一个恶意的对象，RMI通信时会自动将对象进行序列化；
        // 2、客户端、服务端和注册中心，都必须要反序列化对象的class文件，否则无法进行序列化和反序列化；
        // 3、反序列化具有传递性，即 对象内包含的其他对象都会被序列化；



        return null;
    }
}
