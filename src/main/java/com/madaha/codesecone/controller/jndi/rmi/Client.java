package com.madaha.codesecone.controller.jndi.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private Client() {
        System.out.println("TClient 构造函数创建！");
    }

    public static void main(String[] args){
        try{
            Registry registry = LocateRegistry.getRegistry("localhost");
            Hello stub = (Hello) registry.lookup("Hello");
            String response = stub.sayHello();

            System.out.println("response: " + response);

        }catch (Exception e){
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
