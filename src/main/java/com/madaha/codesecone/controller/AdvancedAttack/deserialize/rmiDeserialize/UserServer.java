package com.madaha.codesecone.controller.AdvancedAttack.deserialize.rmiDeserialize;

import com.sun.jndi.rmi.registry.ReferenceWrapper;

import javax.naming.Reference;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class UserServer {

    public static void main(String[] args) throws Exception{
        UserImpl user = new UserImpl();

        LocateRegistry.createRegistry(3333);
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 3333);
        registry.bind("User", user);
    }
}
