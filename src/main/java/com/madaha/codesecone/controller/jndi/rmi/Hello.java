package com.madaha.codesecone.controller.jndi.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 定义远程接口
 */
public interface Hello extends Remote {
    String sayHello() throws RemoteException;
}
